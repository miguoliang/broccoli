package broccoli.model.identity.http.response;

/**
 * The {@link CreateUserResponse} class.
 *
 * @param id        User id
 * @param username  User username
 * @param firstName User first name
 * @param lastName  User last name
 * @param email     User email
 */
public record CreateUserResponse(String id, String username, String firstName, String lastName,
                                 String email) {
}
