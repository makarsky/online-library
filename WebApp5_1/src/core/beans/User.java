/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.beans;

import java.util.ArrayList;

public class User {

    private long id;
    private String name;
    private String password;
    private String role;
    private ArrayList<Long> favorites = new ArrayList<>();
    private String session_id;

    public User(String session_id) {
        this.session_id = session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void addToFavorites(long book_id) {
        favorites.add(book_id);
    }
    
    public void deleteFromFavorites(long book_id) {
        favorites.remove(book_id);
    }

    public ArrayList<Long> getFavorites() {
        return favorites;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
