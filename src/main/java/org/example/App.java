package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {

  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_PURPLE = "\u001B[35m";
  public static void startProgram() {
    System.out.println(
            "*****************************************************************" + "\n" +
                    "*************" + ANSI_PURPLE + "Welcome to Zhunan's Music Manager APP" + ANSI_RESET +"***************" + "\n" +
                    "**********" + ANSI_GREEN + "Please Enter Your User Login ID and Password" + ANSI_RESET + "***********" + "\n" +
                    "*****************************************************************" + "\n"
    );

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.print(">> Please Enter ID: ");
    String s = null;
    try {
      s = br.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public static void main( String[] args )
  {
    startProgram();
  }

}
