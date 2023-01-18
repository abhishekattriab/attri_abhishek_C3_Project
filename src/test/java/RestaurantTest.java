import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    Restaurant restaurant;
    MockedStatic<LocalTime> mockedLocalTimeObject;

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE

    public void setupRestaurant() {

        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = new Restaurant("Amelie's cafe", "Chennai", openingTime, closingTime);
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);

    }

    public void setupMocks(String mockedTime) {

        setupRestaurant();
        LocalTime mockedLocalTime = LocalTime.parse(mockedTime);
        mockedLocalTimeObject = Mockito.mockStatic(LocalTime.class);
        mockedLocalTimeObject.when(LocalTime::now).thenReturn(mockedLocalTime);

    }

    @AfterEach
    public void after() {
        if (mockedLocalTimeObject != null) {
            mockedLocalTimeObject.close();
        }
    }

    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time() {

        setupMocks("15:00:00");
        Assertions.assertTrue(restaurant.isRestaurantOpen());

    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time() {

        setupMocks("08:00:00");
        Assertions.assertFalse(restaurant.isRestaurantOpen());

    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1() {

        setupRestaurant();

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie", 319);
        assertEquals(initialMenuSize + 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws ItemNotFoundException {

        setupRestaurant();

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize - 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {

        setupRestaurant();

        assertThrows(ItemNotFoundException.class,
                () -> restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /* <<<<<<<<<<<< TDD requirements >>>>>>>>>>>>>>>>>>>>>>

        Requirements:
        Once the user has selected a restaurant, he/she can select the items available
        in that restaurant. The total value of the selected should be calculated/updated.

    */

    //<<<<<<<<<<<< TDD failing test case >>>>>>>>>>>>>>>>>>>>>>
    @Test
    public void select_item_from_list_should_not_return_total_order_value() {

        setupRestaurant();
        List<String> selectedItemNames = Arrays.asList("Vegetable lasagne", "Sweet corn soup");
        int totalOrderValue = restaurant.getTotalOrderValue(selectedItemNames);
        assertEquals(388, totalOrderValue);

    }


}