package de.heinerion.betriebe.enums;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class UtilDemo {
  public static void main(String... args) {
    System.out.println(System.getProperty("user.home"));
    System.out.println(System.getProperty("user.dir"));

    JFileChooser fr = new JFileChooser();
    FileSystemView fw = fr.getFileSystemView();
    System.out.println(fw.getDefaultDirectory());
    System.out.println(fw.getHomeDirectory());
    System.out.println(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
    System.out.println(FileSystemView.getFileSystemView().getHomeDirectory().getPath());

    System.exit(0);
  }
}
