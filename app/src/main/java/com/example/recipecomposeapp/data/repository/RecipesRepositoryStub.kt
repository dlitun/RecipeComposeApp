package com.example.recipecomposeapp.data.repository

import com.example.recipecomposeapp.data.dto.CategoryDto
import com.example.recipecomposeapp.data.dto.IngredientDto
import com.example.recipecomposeapp.data.dto.RecipeDto

private val categories = listOf(
    CategoryDto(
        id = 0,
        title = "Бургеры",
        description = "Рецепты всех популярных видов бургеров",
        imageUrl = "burger.png"
    ),
    CategoryDto(
        id = 1,
        title = "Десерты",
        description = "Самые вкусные рецепты десертов специально для вас",
        imageUrl = "dessert.png"
    ),
    CategoryDto(
        id = 2,
        title = "Пицца",
        description = "Пицца на любой вкус и цвет. Лучшая подборка для тебя",
        imageUrl = "pizza.png"
    ),
    CategoryDto(
        id = 3,
        title = "Рыба",
        description = "Печеная, жареная, сушеная, любая рыба на твой вкус",
        imageUrl = "fish.png"
    ),
    CategoryDto(
        id = 4,
        title = "Супы",
        description = "От классики до экзотики: мир в одной тарелке",
        imageUrl = "soup.png"
    ),
    CategoryDto(
        id = 5,
        title = "Салаты",
        description = "Хрустящий калейдоскоп под соусом вдохновения",
        imageUrl = "salad.png"
    )
)

private val burgerRecipes = listOf(
    RecipeDto(
        id = 0,
        title = "Классический бургер с говядиной",
        ingredients = listOf(
            IngredientDto(
                quantity = "0.5",
                unitOfMeasure = "кг",
                description = "говяжий фарш"
            ),
            IngredientDto(
                quantity = "1.0",
                unitOfMeasure = "шт",
                description = "луковица, мелко нарезанная"
            ),
            IngredientDto(
                quantity = "2.0",
                unitOfMeasure = "зубч",
                description = "чеснок, измельченный"
            ),
            IngredientDto(
                quantity = "4.0",
                unitOfMeasure = "шт",
                description = "булочки для бургера"
            ),
            IngredientDto(
                quantity = "4.0",
                unitOfMeasure = "шт",
                description = "листа салата"
            ),
            IngredientDto(
                quantity = "1.0",
                unitOfMeasure = "шт",
                description = "помидор, нарезанный кольцами"
            ),
            IngredientDto(
                quantity = "2.0",
                unitOfMeasure = "ст. л.",
                description = "горчица"
            ),
            IngredientDto(
                quantity = "2.0",
                unitOfMeasure = "ст. л.",
                description = "кетчуп"
            ),
            IngredientDto(
                quantity = "по вкусу",
                unitOfMeasure = "",
                description = "соль и черный перец"
            )
        ),
        method = listOf(
            "1. В глубокой миске смешайте говяжий фарш, лук, чеснок, соль и перец. Разделите фарш на 4 равные части и сформируйте котлеты.",
            "2. Разогрейте сковороду на среднем огне. Обжаривайте котлеты с каждой стороны в течение 4-5 минут или до желаемой степени прожарки.",
            "3. В то время как котлеты готовятся, подготовьте булочки. Разрежьте их пополам и обжарьте на сковороде до золотистой корочки.",
            "4. Смазать нижние половинки булочек горчицей и кетчупом, затем положите лист салата, котлету, кольца помидора и закройте верхней половинкой булочки.",
            "5. Подавайте бургеры горячими с картофельными чипсами или картофельным пюре."
        ),
        imageUrl = "burger-hamburger.png"
    ),
    RecipeDto(
        id = 1,
        title = "Чизбургер с беконом",
        ingredients = listOf(
            IngredientDto(
                quantity = "0.4",
                unitOfMeasure = "кг",
                description = "говяжий фарш"
            ),
            IngredientDto(
                quantity = "4.0",
                unitOfMeasure = "шт",
                description = "ломтика бекона"
            ),
            IngredientDto(
                quantity = "4.0",
                unitOfMeasure = "шт",
                description = "ломтика сыра чеддер"
            ),
            IngredientDto(
                quantity = "4.0",
                unitOfMeasure = "шт",
                description = "булочки для бургера"
            ),
            IngredientDto(
                quantity = "1.0",
                unitOfMeasure = "шт",
                description = "помидор, нарезанный"
            ),
            IngredientDto(
                quantity = "по вкусу",
                unitOfMeasure = "",
                description = "майонез и кетчуп"
            )
        ),
        method = listOf(
            "1. Обжарьте бекон на сковороде до хрустящей корочки, отложите на бумажное полотенце.",
            "2. Сформируйте из фарша 4 котлеты, обжарьте с каждой стороны по 4 минуты.",
            "3. За минуту до готовности положите на каждую котлету по ломтику сыра, чтобы он расплавился.",
            "4. Соберите бургер: булочка, майонез, котлета с сыром, бекон, помидор, кетчуп.",
            "5. Подавайте горячими."
        ),
        imageUrl = "burger-cheeseburger.png"
    )
)

private val dessertRecipes = listOf(
    RecipeDto(
        id = 100,
        title = "Шоколадный брауни",
        ingredients = listOf(
            IngredientDto(
                quantity = "200",
                unitOfMeasure = "г",
                description = "темный шоколад"
            ),
            IngredientDto(
                quantity = "120",
                unitOfMeasure = "г",
                description = "сливочное масло"
            ),
            IngredientDto(
                quantity = "2",
                unitOfMeasure = "шт",
                description = "яйца"
            ),
            IngredientDto(
                quantity = "100",
                unitOfMeasure = "г",
                description = "сахар"
            ),
            IngredientDto(
                quantity = "80",
                unitOfMeasure = "г",
                description = "пшеничная мука"
            )
        ),
        method = listOf(
            "1. Растопите шоколад и масло на водяной бане.",
            "2. Вмешайте сахар и яйца до однородности.",
            "3. Добавьте муку, аккуратно перемешайте.",
            "4. Выпекайте при 180 C около 25 минут."
        ),
        imageUrl = "dessert.png"
    )
)

class RecipesRepositoryStub {

    fun getCategories(): List<CategoryDto> {
        return categories
    }

    fun getRecipesByCategoryId(categoryId: Int): List<RecipeDto> {
        return when (categoryId) {
            0 -> burgerRecipes
            1 -> dessertRecipes
            2 -> emptyList()
            3 -> emptyList()
            4 -> emptyList()
            5 -> emptyList()
            else -> emptyList()
        }
    }
}
