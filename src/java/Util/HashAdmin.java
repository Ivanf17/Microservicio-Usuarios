/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

/**
 *
 * @author flori
 */
public class HashAdmin {
    public static void main(String[] args) {
        String hash = Util.PasswordUtil.hashPassword("admin123");
        System.out.println(hash);
    }
}

