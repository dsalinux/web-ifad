package br.edu.ifnmg.ifad.util;

import java.io.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author danilo
 */
public class FileUtil {

    public enum Diretorio {
        CONFIGURACOES("configuracoes"),
        BANNER("banner");

        private String simpleName;
        
        private Diretorio(String simpleName) {
            this.simpleName = simpleName;
        }

        public String getSimpleName() {
            return simpleName;
        }
        
    }
    
    public static String creatTempFile(String name, byte[] content) {
        try {
            if (name == null || name.isEmpty()) {
                name = "tempFile.tmp";
            }
            name += new Date().getTime() + name;
            File temp = File.createTempFile("file", "temp");
            File tempFile = new File(temp.getParent(), name);
            OutputStream os = new FileOutputStream(tempFile);
            os.write(content);
            os.flush();
            os.close();
            return tempFile.getAbsolutePath();
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static File getTempFile(String pathFileName) {
        File tempFile = new File(pathFileName);
        if (tempFile != null && tempFile.canRead() && tempFile.exists()) {
            return tempFile;
        }
        return null;
    }

    public static File getHomeCliente() {
        String userHome = System.getProperty("user.home");
        File parentFolder = null;
        parentFolder = new File(userHome + "/" + Constants.CLIENTE_NAME);
        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }
        return parentFolder;
    }
    
    /**
     * 
     * @param diretorio
     * @return 
     */
    public static File getDiretorio(Diretorio diretorio){
        File homeCliente = getHomeCliente();
        File dir = new File(homeCliente, diretorio.getSimpleName());
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    /**
     * 
     * @param diretorio
     * @param name
     * @param content
     * @return File Path
     */
    public static String createFileFromDiretorio(Diretorio diretorio, String name, byte[] content){
        try {
            if (name == null || name.isEmpty()) {
                name = "file.tmp";
            }
            name += new Date().getTime() + name;
            File newFile = new File(getDiretorio(diretorio), name);
            OutputStream os = new FileOutputStream(newFile);
            os.write(content);
            os.flush();
            os.close();
            return newFile.getAbsolutePath();
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;    
    }
    
    public static File createFileHomeUser(String name, String parent, byte[] content) {
        OutputStream os = null;
        try {
            String userHome = System.getProperty("user.home");
            File parentFolder = null;
            parentFolder = new File(userHome + "/" + Constants.CLIENTE_NAME + "/" + parent);
            if (!parentFolder.exists()) {
                parentFolder.mkdirs();
            }
            File arquivo = new File(parentFolder, name);
            os = new FileOutputStream(arquivo);
            os.write(content);
            os.flush();
            os.close();
            return arquivo;
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static byte[] fileToByteArray(File file) {

        if (file == null || !file.exists()) {
            System.out.println("Arquivo informado não existe.");
            return null;
        }
        InputStream is = null;
        byte[] bytes = null;
        try {
            is = new FileInputStream(file);
            long length = file.length();
            bytes = new byte[(int) length];
            int offset = 0, n = 0;
            while (offset < bytes.length && (n = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += n;
            }
            is.close();
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bytes;
    }
    
    public static float calcularTamanhoKB(File dir){
        return calcularTamanho(dir) / 1024f;
    }
    
    public static float calcularTamanhoMB(File dir){
        return calcularTamanho(dir) / 1048576f;
    }
    
    public static float calcularTamanhoGB(File dir){
        return calcularTamanho(dir) / 1073741824f;
    }
    
    public static float calcularTamanhoTB(File dir){
        return calcularTamanhoGB(dir) / 1024f;
    }
    
    /**
     * 
     * @param dir
     * @return Length in bytes
     */
    public static long calcularTamanho(File dir) {
        long ret = 0;
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                ret += calcularTamanho(f);
            } else {
                ret += f.length();
            }
        }
        return ret;
    }
}