package com.company;

import java.io.File;

public class MyFile {
    public File current_directory;

    public MyFile(String path) throws FileNotFound {
        current_directory = new File(path);
        if (!current_directory.exists()) {
            current_directory = null;
            throw new FileNotFound("Ошибка!!!", path);
        }
    }

    public static boolean isAbsPuth(String path) {
        if (path.length() == 0) {return false;}
        else {
            if (path.charAt(0) == '/') {
                return true;
            } else {
                return false;
            }
        }
    }

    public File GetFileOrDirectory(String path, TypeOfFile type_of_file) throws FileNotFound {
        File new_dir = null;
        if (isAbsPuth(path)) {
            new_dir = new File(path);
        }
        else {
            new_dir = new File(current_directory.getPath()+"/"+path);
        }

        if (!new_dir.exists()) throw new FileNotFound("Ошибка!!!", "путь "+path);

        if (new_dir.isDirectory() == true && type_of_file == TypeOfFile.Directory) {
            return new_dir;
        }
        else if (new_dir.isFile() == true && type_of_file == TypeOfFile.File) {
            return new_dir;
        }
        else if ((new_dir.isFile() == true || new_dir.isDirectory() == true) && (type_of_file == TypeOfFile.All)) {
            return new_dir;
        }
        else {
            throw new FileNotFound("Ошибка!!!", path);
        }
    }
}

