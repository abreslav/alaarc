# alaarc
Multi-threaded reference counting.

## Prerequisities
- Java 1.8
- Maven

## Build it
    alaarc> mvn package

## Run it
Windows: `alaarc.cmd <arguments>`

Linux: `alaarc.sh <arguments>`

Command-line help:

    alaarc> alaarc.cmd
    Options: <SOURCE_FILE> ...
      <SOURCE_FILE>         source file name
      -asm <FILE>           emit generated code to FILE
      -log <FILE>           write logs to FILE
      -times <N>            run N times
      -help                 prints help

Hello, world:

    alaarc> alaarc.cmd examples/hello.alaarc
    <main @14:19:39> Program started
    <main @14:19:39> Main thread spawned: Alaarc-0
    <Alaarc-0 @14:19:39> @examples/hello.alaarc:1: Hello, world!
    <Alaarc-0 @14:19:39> Thread finished: Alaarc-0
    <Alaarc-0 @14:19:39> Program finished
    --- DONE ---
    Run 0: assertions passed: 0; assertions failed: 0; exceptions: 0
    Total assertions passed: 0
    Total assertions failed: 0
    Total exceptions: 0

## Syntax
    // This is a comment
    
    log "Hello!"            // Posts a message to program log
    
    a = object              // Creates new object (variable 'a' defined)
    a.x1 = object           // Object field
    a.x2 = null             // 'null'
    b ~= a                  // Weak reference
    
    thread {                // Spawn a new thread
      sleepr                // Sleep for a random period (10-100 ms)
      dump a                // Dumps an object
    }
    
    sleep                   // Sleep for 100 ms
    
    assertrc a.x1 >= 1      // Assertion on a reference count 
    