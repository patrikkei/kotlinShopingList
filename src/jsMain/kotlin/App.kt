import csstype.TextDecoration
import emotion.react.css
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul

private val scope = MainScope()

val App = FC<Props> {

    var (shoppingList, setShoppingList) = useState(emptyList<ShoppingListItem>())
    val (deleted, setDeleted) = useState(mutableListOf<ShoppingListItem>())

    useEffectOnce {
        scope.launch {
            setShoppingList(getShoppingList())
        }
    }

    h1 {
        +"Full-Stack Shopping List"
    }

    ul {
        shoppingList.sortedByDescending(ShoppingListItem::priority).forEach { item ->
            li {
                if (item in deleted) {
                    css {
                        textDecoration = TextDecoration.lineThrough
                    }
                }

                key = item.toString()
                onClick = {
                    scope.launch {
                        val newList = deleted.apply { remove(item) }
                        setDeleted(newList)
//                        setShoppingList(getShoppingList())
                    }
                }
                +"[${item.priority}] ${item.desc}"
            }
        }
    }

    InputComponent {
        onSubmit = { input ->
            val cartItem = ShoppingListItem(
                input.replace("!", ""),
                input.count { it == '!' }
            )

            scope.launch {
                addShoppingListItem(cartItem)
                shoppingList = getShoppingList()
            }
        }
    }
}