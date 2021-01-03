package com.shursulei.shellspring;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MyCommands {
    @ShellMethod("Add two integers together.")
    public int add(int a, int b) {
        return a + b;
    }
    @ShellMethod("Display stuff.")
    public String echo(int a, int b, int c) {
        return String.format("You said a=%d, b=%d, c=%d", a, b, c);
    }
    }
