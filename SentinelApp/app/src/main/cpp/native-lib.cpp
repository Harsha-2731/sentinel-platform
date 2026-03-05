#include <asm/unistd.h>
#include <fcntl.h>
#include <jni.h>
#include <string>
#include <sys/syscall.h>
#include <unistd.h>
#include <vector>

// V53: Elite SVC Hardening
// Bypasses libc.so entirely by using direct assembly for SVC (Supervisor Call)
// This makes it virtually impossible for Frida/Zygisk to hook these calls.

// Fallback definitions for syscall numbers (ARM64)
#ifndef __NR_openat
#define __NR_openat 56
#endif
#ifndef __NR_read
#define __NR_read 63
#endif
#ifndef __NR_close
#define __NR_close 57
#endif
#ifndef AT_FDCWD
#define AT_FDCWD -100
#endif

extern "C" {
// Helper for direct assembly syscall (ARM64)
static inline long sentinel_svc_openat(long fd, const char *path, long flags,
                                       long mode) {
  long res;
#if defined(__aarch64__)
  register long x0 __asm__("x0") = fd;
  register long x1 __asm__("x1") = (long)path;
  register long x2 __asm__("x2") = flags;
  register long x3 __asm__("x3") = mode;
  register long x8 __asm__("x8") = __NR_openat;
  __asm__ __volatile__("svc #0"
                       : "=r"(x0)
                       : "r"(x0), "r"(x1), "r"(x2), "r"(x3), "r"(x8)
                       : "memory");
  res = x0;
#else
  res = syscall(SYS_openat, (int)fd, path, (int)flags, (int)mode);
#endif
  return res;
}

static inline long sentinel_svc_read(long fd, void *buf, size_t count) {
  long res;
#if defined(__aarch64__)
  register long x0 __asm__("x0") = fd;
  register long x1 __asm__("x1") = (long)buf;
  register long x2 __asm__("x2") = (long)count;
  register long x8 __asm__("x8") = __NR_read;
  __asm__ __volatile__("svc #0"
                       : "=r"(x0)
                       : "r"(x0), "r"(x1), "r"(x2), "r"(x8)
                       : "memory");
  res = x0;
#else
  res = read((int)fd, buf, count);
#endif
  return res;
}

static inline void sentinel_svc_close(long fd) {
#if defined(__aarch64__)
  register long x0 __asm__("x0") = fd;
  register long x8 __asm__("x8") = __NR_close;
  __asm__ __volatile__("svc #0" : "=r"(x0) : "r"(x0), "r"(x8) : "memory");
#else
  close((int)fd);
#endif
}
}

// Simple de-obfuscation (XOR with 0x42)
static std::string deobfuscate(const std::string &input) {
  std::string output = input;
  for (size_t i = 0; i < input.size(); i++) {
    output[i] = input[i] ^ (char)0x42;
  }
  return output;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_sentinel_agent_utils_SecurityHelper_isNativeTamperDetected(
    JNIEnv *env, jobject thiz) {
  // V53: Use Elite SVC Handlers
  // Target: "/proc/self/maps"
  long fd = sentinel_svc_openat(AT_FDCWD, "/proc/self/maps", O_RDONLY, 0);
  if (fd < 0)
    return JNI_FALSE;

  char buffer[4096];
  long bytesRead;
  bool tamperFound = false;

  // Target keywords (Obfuscated XOR 0x42)
  // "frida"   ^ 0x42 = "DRKBC"
  // "zygisk"  ^ 0x42 = "X[IKQU"
  // "magisk"  ^ 0x42 = "OCEKQU"
  // "xposed"  ^ 0x42 = "ZEQUBC"
  // "lsposed" ^ 0x42 = "NQXQUBC"
  std::vector<std::string> targets = {"DRKBC", "X[IKQU", "OCEKQU", "ZEQUBC",
                                      "NQXQUBC"};

  // V53: Self-Integrity Check
  // "sentinel_native" -> "UCPRGPLC_LCRKXG" (XOR 0x42)
  bool selfFound = false;
  std::string selfLib = deobfuscate("UCPRGPLC_LCRKXG");

  // Reset file for a clean second pass or search within the same pass
  sentinel_svc_close(fd);
  fd = sentinel_svc_openat(AT_FDCWD, "/proc/self/maps", O_RDONLY, 0);
  if (fd < 0)
    return JNI_FALSE;

  while ((bytesRead = sentinel_svc_read(fd, buffer, sizeof(buffer) - 1)) > 0) {
    buffer[bytesRead] = '\0';
    std::string content(buffer);

    // Check for execution permissions (r-xp)
    // "r-xp" -> "P/ZP" (XOR 0x42)
    if (content.find(selfLib) != std::string::npos) {
      if (content.find(deobfuscate("P/ZP")) != std::string::npos) {
        selfFound = true;
      }
    }

    for (const auto &t : targets) {
      if (content.find(deobfuscate(t)) != std::string::npos) {
        tamperFound = true;
        break;
      }
    }
    if (tamperFound)
      break;
  }

  sentinel_svc_close(fd);

  // If we can't find ourselves or we are tampered, return true (tamper
  // detected)
  return (tamperFound || !selfFound) ? JNI_TRUE : JNI_FALSE;
}
