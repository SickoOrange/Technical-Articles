import java.io.*;

public class Main {

    public static void main(String[] args) {

        String src = "/Users/yayin/Desktop/mybatis-3/src";
        String des = "/Users/yayin/Desktop/copy";
        copy(src, des);
        System.out.println("finish!");
    }


    /**
     * 将对应目录的所有子目录跟子文件递归的复制到指定的路径
     *
     * @param srcPath  源目录
     * @param destPath 指定目录
     */
    public static void copy(String srcPath, String destPath) {
        File source = new File(srcPath);
        String[] files = source.list();
        //当前目录含有子文件或者子目录才会复制
        if (files != null && files.length > 0) {
            File dest = new File(destPath);
            if (!dest.exists()) {
                dest.mkdir();
            }
            for (String file : files) {
                File child = buildFile(source, file);
                try {
                    if (!child.isDirectory()) {
                        moveFile(child.getCanonicalPath(), destPath + File.separator + child.getName());
                    } else {
                        copy(child.getCanonicalPath(), destPath + File.separator + child.getName());
                    }
                } catch (IOException e) {
                    System.out.println("An error was occurred while manipulating file %s" + child.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println(String.format("the specified director %s is empty!", source.getName()));
        }


    }

    private static File buildFile(File source, String file) {
        return new File(source, file);
    }

    public static void moveFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);

        try {
            FileInputStream in = new FileInputStream(oldFile);
            FileOutputStream out = new FileOutputStream(newFile);
            byte[] bytes = new byte[1024];
            while(in.read(bytes)!=-1){
                out.write(bytes);
            }
        } catch (IOException e) {
            System.out.println("move file: " + oldFile.getName());
            System.out.println("from: " + oldFile.getAbsolutePath());
            System.out.println("to: " + newFile.getAbsolutePath());
            System.out.println("failed!");
            e.printStackTrace();
        }

    }


}
