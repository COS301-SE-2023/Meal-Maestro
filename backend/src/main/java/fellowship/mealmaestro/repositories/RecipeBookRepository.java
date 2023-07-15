@Repository
public class RecipeBookRepository {
    
    @Autowired
    private final Driver driver;

    public RecipeBookRepository(Driver driver){
        this.driver = driver;
    }

    //#region Create
    public void addToRecipeBook(RecipeModel recipe){
        try (Session session = driver.session()){
            session.executeWrite(addToRecipeBookTransaction(recipe));
        }
    }

    public static TransactionCallback<Void> addToRecipeBookTransaction(RecipeModel recipe) {
        return transaction -> {
            transaction.run("CREATE (:RecipeBook)-[:CONTAINS]->(:Recipe {name: $name, description: $description})",
                Values.parameters("name", recipe.getName(), "description", recipe.getDescription()));
            return null;
        };
    }
    //#endregion

    //#region Read
    public List<RecipeModel> getRecipeBook(){
        try (Session session = driver.session()){
            return session.executeRead(getRecipeBookTransaction());
        }
    }

    public static TransactionCallback<List<RecipeModel>> getRecipeBookTransaction() {
        return transaction -> {
            var result = transaction.run("MATCH (:RecipeBook)-[:CONTAINS]->(r:Recipe) RETURN r.name AS name, r.description AS description");
            
            List<RecipeModel> recipes = new ArrayList<>();
            while (result.hasNext()){
                var record = result.next();
                recipes.add(new RecipeModel(record.get("name").asString(), record.get("description").asString()));
            }
            return recipes;
        };
    }
    //#endregion

    //#region Delete
    public void removeFromRecipeBook(String recipeName){
        try (Session session = driver.session()){
            session.executeWrite(removeFromRecipeBookTransaction(recipeName));
        }
    }

    public static TransactionCallback<Void> removeFromRecipeBookTransaction(String recipeName) {
        return transaction -> {
            transaction.run("MATCH (:RecipeBook)-[:CONTAINS]->(r:Recipe {name: $name}) DETACH DELETE r",
                Values.parameters("name", recipeName));
            return null;
        };
    }
    //#endregion
}
