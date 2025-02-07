/*
 * @Author: mirroee 3323458794@qq.com
 * @Date: 2025-02-08 03:37:46
 * @LastEditors: mirroee 3323458794@qq.com
 * @LastEditTime: 2025-02-08 05:07:12
 * @FilePath: \ASE\MenuItem.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */

import java.time.LocalDateTime;
import java.util.HashMap;

public class MenuItem {
    private String name;
    private String category;//int? 1:beverage 2:food 3:dessert
    private float cost;
    private String identifier;
    private String description;

    private static final String[] ALLOWED_CATEGORIES = {"beverage", "food", "dessert"};

    public MenuItem(String name,String category,float cost,String identifier,String description){
        try {
            validateMenuItem(name, category, cost, identifier, description);
        } catch (GenerateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.identifier = identifier;
        this.description = description;
    }

    private void validateMenuItem(String name, String category, float cost, String identifier, String description) throws GenerateException {
        if (name == null || name.trim().isEmpty()) {
            throw new GenerateException("Menu item generate error: Name is empty.") ;
        }
        if (!isValidCategory(category)) {
            throw new GenerateException("Menu item generate error: Category is invalid, must be one of beverage, food or dessert.") ;
        
        if (cost <= 0 || cost >= 1000) {
            throw new GenerateException("Menu item generate error: The price must be a positive number between 0 and 1000.") ; }
        }
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new GenerateException("Menu item generate error: Identifier is null.") ;
        }
        if (description == null || description.trim().isEmpty()) {
            throw new GenerateException("Menu item generate error: Description is null.") ;
        }
    }

    private boolean isValidCategory(String category) {
        for (String validCategory : ALLOWED_CATEGORIES) {
            if (validCategory.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public float getCost() {
        return cost;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }
}
