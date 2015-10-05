/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Images;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
/**
 *
 * @author Ivan
 */
public class ImagesLoader {
    public static String DIR = "/Resources";
    public static String DIRFOLDER = "/Resources/Imagenes/";
    private HashMap<String, ArrayList> imagenes;
    
    public ImagesLoader(String nombreArchivo){
        imagenes = new HashMap<>();
        cargarDesdeArchivo(nombreArchivo);
    }
    
    private void cargarDesdeArchivo(String nombreArchivo) {
        try {
            InputStream in = getClass().getResourceAsStream("/ImagenesText/" + nombreArchivo);
            //FileInputStream fis = new FileInputStream(new File(DIR + nombreArchivo));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String linea;
            char ch;
            try {
                while((linea = br.readLine()) != null) {
                    if(linea.length() == 0) //linea en blanco
                        continue;
                    if(linea.startsWith("//")) //comentario
                        continue;
                    ch = Character.toLowerCase(linea.charAt(0));
                    if(ch == 'e')
                        lineaEstatica(linea);
                    if(ch == 't') //t significa tira
                        lineaTira(linea);
                    if(ch == 'f')
                        lineaFont(linea);
                    if(ch == 's')
                        sheetLine(linea);
                }
                br.close();
                //fis.close();
            } catch (IOException ex) {
                System.out.println("Error de lectura");
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Archivo no encontrado");
        }
    }
    
    private void lineaEstatica(String linea) {
        System.out.println("Reading static line");
        StringTokenizer token = new StringTokenizer(linea);
        if(token.countTokens() != 2)
            System.out.println("Numero equivocado de argumentos en " + linea);
        else{
            token.nextToken();
            String archivo = token.nextToken();
            cargarEstatica(archivo);
        }
    }
    
    private void sheetLine(String line) {
        StringTokenizer token = new StringTokenizer(line);
        if(token.countTokens() != 4)
            System.out.println("Numero equivocado de argumentos en " + line);
        else {
            token.nextToken();
            String file = token.nextToken();
            int width = Integer.parseInt(token.nextToken());
            int height = Integer.parseInt(token.nextToken());
            loadSpriteSheet(file, width, height);
        }
    }
    
    private void loadSpriteSheet(String file, int width, int height) {
        String key = prefijo(file);
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/Imagenes/" + file));
            BufferedImage subimage;
            int framesNumber = image.getWidth() / width;
            int spritesNumber = image.getHeight() / height;
            ArrayList<BufferedImage> sprites;
            sprites = new ArrayList<>();
            
            for(int i = 0; i < spritesNumber; i++) {
                for(int j = 0; j < framesNumber; j++) {
                    subimage = image.getSubimage(width * j, height * i, width, height);
                    sprites.add(image);
                }
                if(spritesNumber == 1) imagenes.put(key, sprites);
                else imagenes.put(key + i, sprites);
            }
        }
        catch(Exception e) { e.printStackTrace(); }
    }
    
    private void lineaFont(String linea){
        StringTokenizer token = new StringTokenizer(linea);
        if(token.countTokens() != 2)
            System.out.println("Numero equivocado de argumentos en " + linea);
        else{
            token.nextToken();
            String archivo = token.nextToken();
            cargarFont(archivo);
        }
    }
    
    private void cargarFont(String archivo){
        String clave = prefijo(archivo);
        if(imagenes.containsKey(clave))
            System.out.println("La clave ya existe");
        else{
            BufferedImage[][] ch = cargarArregloFont(archivo);
            if(ch == null)
                System.out.println("Tira nula");
            else{
                ArrayList<BufferedImage> listaImagenes = new ArrayList<>();
                for(int i=0; i<16; i++){
                    for(int j=0; j<16; j++)
                        listaImagenes.add(ch[i][j]);
                }
                imagenes.put(clave, listaImagenes);
                System.out.println("Font cargado");
            }
        }
    }
    
    private void cargarEstatica(String archivo){
        String clave = prefijo(archivo);
        try {
            BufferedImage estatica = cargarImagen(archivo);
            if(imagenes.containsKey(clave))
                System.out.println("La clave ya existe");
            else {
                if(estatica == null)
                    System.out.println("Imagen nula");
                else {
                    ArrayList<BufferedImage> listaImagenes = new ArrayList<>();
                    listaImagenes.add(estatica);
                    imagenes.put(clave, listaImagenes);
                }
            }
        }
        catch(Exception e) {
            System.out.println("Archivo " + archivo + " inexistente");
        }
        
    }
    
    private BufferedImage cargarImagen(String archivo){
        try {
            System.out.println("Archivo: " + archivo);
            BufferedImage im = ImageIO.read(getClass().getResourceAsStream("/Imagenes/" + archivo));
            if(im == null)
                im = new BufferedImage(100, 100, 0);
            return im;
        } catch (IOException ex) {
        }
        
        return null;
    }
    
    private void lineaTira(String linea){
        StringTokenizer token = new StringTokenizer(linea);
        if(token.countTokens() != 3)
            System.out.println("Numero equivocado de argumentos en " + linea);
        else{
            token.nextToken();
            String archivo = token.nextToken();
            int numImagenes = -1;
            try{
                numImagenes = Integer.parseInt(token.nextToken());
            }
            catch(Exception e){
                System.out.println("Numero incorrecto para " + linea);
            }
            cargarTira(archivo, numImagenes);
        }
    }
    
    private void cargarTira(String archivo, int numImagenes){
        String clave = prefijo(archivo);
        if(imagenes.containsKey(clave))
            System.out.println("La clave ya existe");
        else{
            BufferedImage[] tira = cargarArregloTira(archivo, numImagenes);
            if(tira == null)
                System.out.println("Tira nula");
            else{
                ArrayList<BufferedImage> listaImagenes = new ArrayList<>();
                for(int i=0; i<tira.length; i++){
                    listaImagenes.add(tira[i]);
                }
                imagenes.put(clave, listaImagenes);
                System.out.println(archivo + " cargado");
            }
        }
    }
    
    private BufferedImage[][] cargarArregloFont(String archivo){
        BufferedImage tiraCompleta;
        BufferedImage[][] imagenesTira = new BufferedImage[16][16];
        Graphics gCopiado;
        
        int nWidth;
        int nHeight;
        
        try {
            tiraCompleta = ImageIO.read(this.getClass().getResourceAsStream("/Imagenes/" + archivo));
            nWidth = tiraCompleta.getWidth()/16;
            nHeight = tiraCompleta.getHeight()/16;
            
            for(int i=0; i<16; i++){
                for(int j=0; j<16; j++){
                    imagenesTira[i][j] = new BufferedImage(nWidth, nHeight, tiraCompleta.getTransparency());
                    gCopiado = imagenesTira[i][j].getGraphics();
                    gCopiado.drawImage(tiraCompleta, 0, 0, nWidth, nHeight, j*nWidth, i*nHeight, (j*nWidth)+nWidth, (i*nHeight)+nHeight, null);
                    gCopiado.dispose();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return imagenesTira;
    }
    // Por corregir que no se queje si no esta la imagen
    private BufferedImage[] cargarArregloTira(String archivo, int numImagenes) {
        BufferedImage tiraCompleta;
        BufferedImage[] imagenesTira = new BufferedImage[numImagenes];
        Graphics gCopiado;
        
        int nWidth;
        int nHeight;
        
        try {
            tiraCompleta = ImageIO.read(this.getClass().getResourceAsStream("/Imagenes/" + archivo));
            nWidth = tiraCompleta.getWidth()/numImagenes;
            nHeight = tiraCompleta.getHeight();
            
            for(int i=0; i<numImagenes; i++){
                imagenesTira[i] = new BufferedImage(nWidth, nHeight, tiraCompleta.getTransparency());
                gCopiado = imagenesTira[i].getGraphics();
                gCopiado.drawImage(tiraCompleta, 0, 0, nWidth, nHeight, i*nWidth, 0, (i*nWidth)+nWidth, nHeight, null);
                gCopiado.dispose();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error en " + archivo);
        }
        return imagenesTira;
    }
    
    private String prefijo(String archivo){
        int posA;
        int posB;
        if((posB = archivo.lastIndexOf(".")) == -1){
            System.out.println("No se encontro el prefijo del archivo");
            return archivo;
        }
        else if(archivo.contains("/")) {
            posA = archivo.lastIndexOf("/");
            return archivo.substring(posA + 1, posB);
        }
        else
            return archivo.substring(0, posB);
    }
    
    public boolean estaCargado(String nombre){
        if(imagenes.containsKey(nombre))
            return true;
        return false;
    }
    
    public int numImagenes(String nombre){
        return imagenes.get(nombre).size();
    }
    
    public BufferedImage getImagen(String nombre){
        return (BufferedImage)imagenes.get(nombre).get(0);
    }
    /*
    public int getWidth(String n) { 
        if(imagenes.containsKey(n))
            return ((BufferedImage)imagenes.get(n).get(0)).getWidth(); 
        System.out.println("Width desconocido. Se devolvera 0");
        return 0;
    }
    public int getHeight(String n) { 
        if(imagenes.containsKey(n))
            return ((BufferedImage)imagenes.get(n).get(0)).getHeight(); 
        System.out.println("Height desconocido. Se devolvera 0");
        return 0;
    }
    */
    
    public int getWidth(String n) { return ((BufferedImage)imagenes.get(n).get(0)).getWidth(); }
    public int getHeight(String n) { return ((BufferedImage)imagenes.get(n).get(0)).getHeight(); }
    public BufferedImage getImage(String n) { return (BufferedImage)imagenes.get(n).get(0); }
    public BufferedImage getImageAt(String n, int index) { return (BufferedImage)imagenes.get(n).get(index); }
    public ArrayList<BufferedImage> getFrames(String n) { return imagenes.get(n); }
    
    public BufferedImage getImagenAt(String nombre, int pos){
        return (BufferedImage)imagenes.get(nombre).get(pos);
    }
    
    public int getWidthImagen(String nombre){
        return ((BufferedImage)imagenes.get(nombre).get(0)).getWidth();
    }
    
    public int getHeightImagen(String nombre){
        return ((BufferedImage)imagenes.get(nombre).get(0)).getHeight();
    }
    
    public static BufferedImage load(String s) {
        try {
            BufferedImage image = ImageIO.read(new File(s));
            return image;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
