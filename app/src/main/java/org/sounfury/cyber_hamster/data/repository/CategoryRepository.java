package org.sounfury.cyber_hamster.data.repository;

import org.sounfury.cyber_hamster.data.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    
    // 获取所有分类
    public List<Category> getAllCategories() {
        // 实际项目中，这里应该从网络或数据库获取数据
        // 此处使用模拟数据
        return getMockCategories();
    }
    
    // 根据ID获取分类
    public Category getCategoryById(int id) {
        List<Category> categories = getMockCategories();
        for (Category category : categories) {
            if (category.getId() == id) {
                return category;
            }
        }
        return null;
    }
    
    // 添加分类
    public boolean addCategory(Category category) {
        // 实现添加分类的逻辑
        return true;
    }
    
    // 更新分类
    public boolean updateCategory(Category category) {
        // 实现更新分类的逻辑
        return true;
    }
    
    // 删除分类
    public boolean deleteCategory(int id) {
        // 实现删除分类的逻辑
        return true;
    }
    
    // 获取模拟数据
    private List<Category> getMockCategories() {
        List<Category> categories = new ArrayList<>();
        
        categories.add(new Category(1, "小说", "文学小说类"));
        categories.add(new Category(2, "科技", "科学技术类"));
        categories.add(new Category(3, "社科", "社会科学类"));
        categories.add(new Category(4, "教育", "教育相关类"));
        categories.add(new Category(5, "经济", "经济管理类"));
        
        return categories;
    }
} 