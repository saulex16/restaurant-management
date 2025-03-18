package at.technikum_wien.restaurant_management.repository;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.repository.interfaces.IngredientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class IngredientRepositoryImpl implements IngredientRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Ingredient createIngredient(String name, double price) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setPrice(price);
        entityManager.persist(ingredient);

        return ingredient;
    }

    @Override
    public Optional<Ingredient> getIngredientById(Long id) {
        return Optional.ofNullable(entityManager.find(Ingredient.class, id));
    }

    @Override
    public List<Ingredient> getAllIngredientsById(List<Long> ids) {
        TypedQuery<Ingredient> query = entityManager.createQuery(
                "SELECT i FROM Ingredient i WHERE i.id IN :ids", Ingredient.class
        );

        query.setParameter("ids", ids);

        return query.getResultList();
    }

    @Override
    public Ingredient updateIngredientName(Long id, String name) {
        Optional<Ingredient> maybeIngredient = getIngredientById(id);
        if (maybeIngredient.isPresent()) {
            Ingredient oldIngredient = maybeIngredient.get();
            oldIngredient.setName(name);
            entityManager.merge(oldIngredient);
            return oldIngredient;
        }
        else {
            throw new EntityNotFoundException("Ingredient with ID " + id + " not found");
        }
    }

    @Override
    public Ingredient updateIngredientPrice(Long id, double price) {
        Optional<Ingredient> maybeIngredient = getIngredientById(id);

        if (maybeIngredient.isPresent()) {
            Ingredient oldIngredient = maybeIngredient.get();
            oldIngredient.setPrice(price);
            entityManager.merge(oldIngredient);
            return oldIngredient;
        }
        else {
            throw new EntityNotFoundException("Ingredient with ID " + id + " not found");
        }
    }

    @Override
    public void deleteIngredient(Long id) {
        Optional<Ingredient> maybeIngredient = getIngredientById(id);
        if (maybeIngredient.isPresent()) {
            Ingredient ingredient = maybeIngredient.get();
            entityManager.remove(ingredient);
        } else {
            throw new EntityNotFoundException("Ingredient with ID " + id + " not found");
        }
    }
}
