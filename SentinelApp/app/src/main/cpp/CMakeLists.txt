cmake_minimum_required(VERSION 3.18.1)

project("sentinel_native")

add_library(
        sentinel_native
        SHARED
        native-lib.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        sentinel_native
        ${log-lib})
