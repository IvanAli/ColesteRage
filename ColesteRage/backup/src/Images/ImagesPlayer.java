/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Images;

import java.awt.image.BufferedImage;

/**
 *
 * @author Ivan
 */
public class ImagesPlayer {
    private String nombre;
    private long periodoAnimacion;
    private int duracionImagen;
    private double duracionTotal;
    private long animacionTotal;
    private int numImagenes;
    private int posicionImagen;
    private boolean loop;
    private boolean ticksIgnorados;
    private ImagesLoader cImagenes;
    
    public ImagesPlayer(String clave, long periodo, double duracion, boolean repeticion, ImagesLoader ci){
        nombre = clave;
        periodoAnimacion = periodo;
        duracionTotal = duracion;
        loop = repeticion;
        cImagenes = ci;
        
        animacionTotal = 0L;
        if(!cImagenes.estaCargado(nombre)){
            System.out.println("No se encontro esa clave en el mapa");
            ticksIgnorados = true;
            numImagenes = 0;
            posicionImagen = -1;
        }
        else{
            ticksIgnorados = false;
            numImagenes = cImagenes.numImagenes(nombre);
            posicionImagen = 0;
            duracionImagen = (int)((duracionTotal*1000)/numImagenes);
        }
    }
    
    public void reproducir(){
        if(!ticksIgnorados){
            animacionTotal = (animacionTotal + periodoAnimacion) % (long)(1000 * duracionTotal);
            posicionImagen = (int)(animacionTotal / duracionImagen);
        }
    }
    
    public int getPosicionImagen(){
        return posicionImagen;
    }
    
    public BufferedImage imagenActual(){
        return cImagenes.getImagenAt(nombre, posicionImagen);
    }
    
    public BufferedImage getImagenAt(int pos){
        return cImagenes.getImagenAt(nombre, pos);
    }
}
