package kz.aitu.service;

import kz.aitu.exception.DuplicateResourceException;
import kz.aitu.exception.ResourceNotFoundException;
import kz.aitu.model.MenuItem;
import kz.aitu.repository.MenuItemRepository;

import java.util.List;

public class MenuItemService {

    private final MenuItemRepository repo = new MenuItemRepository();

    public MenuItem create(MenuItem item) {
        item.validate();

        boolean duplicate = repo.getAll().stream().anyMatch(x ->
                x.getName().equalsIgnoreCase(item.getName()) &&
                        x.getEntityType().equals(item.getEntityType())
        );

        if (duplicate) {
            throw new DuplicateResourceException("Duplicate menu item: " + item.getName()
                    + " (" + item.getEntityType() + ")");
        }

        return repo.create(item);
    }

    public List<MenuItem> getAll() {
        return repo.getAll();
    }

    public MenuItem getById(int id) {
        MenuItem item = repo.getById(id);
        if (item == null) {
            throw new ResourceNotFoundException("Menu item not found id=" + id);
        }
        return item;
    }

    public void update(int id, MenuItem item) {
        item.validate();

        if (repo.getById(id) == null) {
            throw new ResourceNotFoundException("Menu item not found id=" + id);
        }
        repo.update(id, item);
    }

    public void delete(int id) {
        if (repo.getById(id) == null) {
            throw new ResourceNotFoundException("Menu item not found id=" + id);
        }
        repo.delete(id);
    }
}
