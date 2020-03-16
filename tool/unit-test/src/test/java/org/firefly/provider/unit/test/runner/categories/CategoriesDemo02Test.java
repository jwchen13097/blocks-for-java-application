package org.firefly.provider.unit.test.runner.categories;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@IncludeCategory(SlowTest.class)
@ExcludeCategory(FastTest.class)
@SuiteClasses({A.class, B.class})
public class CategoriesDemo02Test {
    // Will run A.a2, but not A.a1 or B.b1
}
