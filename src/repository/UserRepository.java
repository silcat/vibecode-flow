import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final Map<String, User> store = new HashMap<>();

    public void save(User user) {
        if (store.containsKey(user.getEmail())) {
            throw new DuplicateEmailException(user.getEmail());
        }
        store.put(user.getEmail(), user);
    }

    public User findByEmail(String email) {
        return store.get(email);
    }
}