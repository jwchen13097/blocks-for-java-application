package org.firefly.provider.unit.test.runner.categories;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;

import static org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@IncludeCategory(SlowTest.class)
@SuiteClasses({A.class, B.class})
public class CategoriesDemo01Test {
    // Will run A.a2 and B.b1, but not A.a1
}
