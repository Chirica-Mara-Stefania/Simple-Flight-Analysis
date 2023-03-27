package service;

import domain.Flight;
import repository.Repository;

import java.util.ArrayList;

public class Service {
    private Repository repo;

    public Service(Repository repo) {
        this.repo = repo;
    }

    public void add(Flight obj) throws Exception {
        if (!repo.add(obj))
            throw new Exception("This flight info already exists!");
    }

    public void update(Flight obj) throws Exception {
        if (!repo.update(obj))
            throw new Exception("Couldn't update flight!");
    }

    public ArrayList<Flight> getAll() throws Exception {
        return repo.getAll();
    }
}
