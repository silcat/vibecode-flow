public class UserRepositoryTest {
    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        // Test 1: save then findByEmail
        try {
            UserRepository repo = new UserRepository();
            User user = new User("test@example.com");
            repo.save(user);
            User found = repo.findByEmail("test@example.com");
            assert found != null : "findByEmail should return saved user";
            assert found.getEmail().equals("test@example.com") : "email should match";
            System.out.println("  PASS: shouldFindSavedUserByEmail");
            passed++;
        } catch (Throwable e) {
            System.out.println("  FAIL: shouldFindSavedUserByEmail - " + e.getMessage());
            failed++;
        }

        // Test 2: duplicate email throws DuplicateEmailException
        try {
            UserRepository repo = new UserRepository();
            repo.save(new User("dup@example.com"));
            repo.save(new User("dup@example.com"));
            System.out.println("  FAIL: shouldThrowOnDuplicateEmail - no exception thrown");
            failed++;
        } catch (DuplicateEmailException e) {
            System.out.println("  PASS: shouldThrowOnDuplicateEmail");
            passed++;
        } catch (Throwable e) {
            System.out.println("  FAIL: shouldThrowOnDuplicateEmail - wrong exception: " + e.getClass().getName());
            failed++;
        }

        System.out.println();
        System.out.println("Result: " + passed + "/" + (passed + failed) + " PASS");
        System.exit(failed > 0 ? 1 : 0);
    }
}