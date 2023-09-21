package fellowship.mealmaestro.models.neo4j;

import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import fellowship.mealmaestro.models.auth.AuthorityRoleModel;
import fellowship.mealmaestro.models.neo4j.relationships.HasLogEntry;
import fellowship.mealmaestro.models.neo4j.relationships.HasMeal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Node("User")
public class UserModel implements UserDetails {

    @NotBlank(message = "A Username is required")
    private String name;

    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @Id
    @NotBlank
    @Email(message = "Email must be valid")
    private String email;

    private AuthorityRoleModel authorityRole;

    @Version
    private Long version;

    @Relationship(type = "HAS_PANTRY", direction = Relationship.Direction.OUTGOING)
    private PantryModel pantry;

    @Relationship(type = "HAS_LIST", direction = Relationship.Direction.OUTGOING)
    private ShoppingListModel shoppingList;

    @Relationship(type = "HAS_SETTINGS", direction = Relationship.Direction.OUTGOING)
    private SettingsModel settings;

    @Relationship(type = "HAS_RECIPE_BOOK", direction = Relationship.Direction.OUTGOING)
    private RecipeBookModel recipeBook;

    @Relationship(type = "HAS_MEAL")
    private List<HasMeal> meals;

    @Relationship(type = "HAS_LOG_ENTRY")
    private List<HasLogEntry> entries;

    @Relationship(type = "HAS_VIEW", direction = Relationship.Direction.OUTGOING)
    private ViewModel view;

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<HasLogEntry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<HasLogEntry> entries) {
        this.entries = entries;
    }

    public ViewModel getView() {
        return this.view;
    }

    public void setView(ViewModel view) {
        this.view = view;
    }

    public UserModel() {
        this.authorityRole = AuthorityRoleModel.USER;
    }

    public UserModel(String name, String password, String email, AuthorityRoleModel authorityRole) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.authorityRole = AuthorityRoleModel.USER;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AuthorityRoleModel getAuthorityRole() {
        return this.authorityRole;
    }

    public void setAuthorityRole(AuthorityRoleModel authorityRole) {
        this.authorityRole = authorityRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authorityRole.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public PantryModel getPantry() {
        return pantry;
    }

    public void setPantry(PantryModel pantry) {
        this.pantry = pantry;
    }

    public ShoppingListModel getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingListModel shoppingList) {
        this.shoppingList = shoppingList;
    }

    public SettingsModel getSettings() {
        return settings;
    }

    public void setSettings(SettingsModel settings) {
        this.settings = settings;
    }

    public RecipeBookModel getRecipeBook() {
        return recipeBook;
    }

    public void setRecipeBook(RecipeBookModel recipeBook) {
        this.recipeBook = recipeBook;
    }

    public List<HasMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<HasMeal> meals) {
        this.meals = meals;
    }

    public List<HasLogEntry> getLogEntries() {
        return entries;
    }

    public void setLogEntries(List<HasLogEntry> entries) {
        this.entries = entries;
    }
}
