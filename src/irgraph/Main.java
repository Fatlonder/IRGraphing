/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irgraph;

/**
 *
 * @author F
 */
public class Main {

    public static void main(String[] args) {
        Model theModel = new Model();
        View theView = new View();
        Controller theController = new Controller(theModel, theView);

    }
}
