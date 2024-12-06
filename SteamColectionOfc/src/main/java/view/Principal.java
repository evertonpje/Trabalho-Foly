/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package view;

import java.util.List;
import model.Jogo;
import model.dao.DaoFactory;
import model.dao.JogoDaoJDBC;

/**
 *
 * @author evert
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            JogoDaoJDBC dao = DaoFactory.novoJogoDao();

          Jogo c = new Jogo("Elden ring", "historia", "Steam", "22/04/2020", "Completado", "./seila/caminho");
            Jogo c1 = new Jogo("Elden ring teste", "historia", "Steam", "22/04/2020", "Completado", "./seila/caminho");
            Jogo c2 = new Jogo("Elden ring teste 4", "historia", "Steam", "22/04/2020", "Completado", "./seila/caminho");
          dao.incluir(c);
            dao.incluir(c1);
            dao.incluir(c2);
          
          
          
            List<Jogo> lista = dao.listar();
            
            for (Jogo jogo : lista) {
                System.out.println(jogo);
                
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
