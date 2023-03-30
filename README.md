# ![image](https://user-images.githubusercontent.com/104612887/227734156-42b96841-ff9c-4a66-a420-97a99a2386db.png)

This project is still not deployed, but until then, you can find below details and some application pictures.

# Table of Contents
  - <a href="#about">About this Project</a>
  - <a href="#design">Design</a>
  
  - <a href="#features">Features</a>
  - <a href="#future-features">Future Features</a>
  - <a href="#project-structure">Project Structure</a>
  - <a href="#email">Email</a>
  - <a href="#application-pictures">Application Pictures</a>

# <p id="about">About this project</p>

ShopLizt is a web application that allows users to plan their grocery shopping by creating an items list in advance. <br>
It offers an option to add and share recipe ideas with other users, also get inspired by others recommendations.

# <p id="design">Design</p>

The amazing design was provided by <a href="https://bootstrapmade.com/">BootstrapMade</a>.

Here you can find more about the design: <a href="https://bootstrapmade.com/demo/templates/Yummy/">Yummy Design</a>

# <p id="features">Features</p>
 - <strong>Authentication</strong>
    - Login
      - log in with existing account

    - Register
      - create new account
 
 
 - <strong>Dashboard (Home page)</strong>

    - My collection (section) 
      - view your recipe collection (own recipes and favorite list (saved recipes posted by other users))
      - option to open or remove a recipe from your Collection
      - option to add new recipe

    - My shopping list (section)
      - view your prepared item list (added products so far) 
      - option to go to your Item list page 

    -	Inspiration (section)
        -	view some recently added recipes by others and add them to your Collection



 - <strong>Item list</strong>

    - My shopping list
      - review your item list
      - add new item (from Navigation bar)
      - move up/down items so that you can order based on your criterias
      - delete item 
      - edit the item text
      - mark (as done) each item when you buy it in the store
      
    - History of bought products (section)
      - when you mark an item as "Bought", it goes to History table, where you can add it to your shopping list again 
      - Reuse button - return item to your product list for your next shopping by reviewing the last products you have bought (ordered by recent date)
      - delete item from your History list



 - <strong>My Collection</strong>
  
      - Users can view their own created recipes and also the receipes they have saved as Favorites


 - <strong>Recipes</strong>
 
    - Pictures: users can add multiple pictures to their own receipes
    - Description: users can add and edit the recipe description
    - Ingredients: 
        - users can list the needed products for the recipe
        - other users can add these products to their shopping list, if they decide to cook with recipe
    - Filter by Categories - users can filter recipes by Category
    - Search Recipe - users can search (input free text) in all existing recipes and find each recipe containing this text in its title or desciption
    - Owners of the recipes can delete the recipe



 - <strong>Comments</strong>

      - users can post comment to each recipe
      - Rating - when posting a comment, users should rate the recipe (1-5)
      - Last 3 comments are visible below the recipe content
      - There is an option to view all comments for the recipe



 - <strong>Administration</strong>

      - Admin user(s) can edit all recipes
      -	Admin user(s) can review all new added comments and should approve or delete any inappropriate comments
      -	Admin user(s) receive daily emails if there are new added comments, awaitng approval
      -	Admin user(s) can access the user list (usernames) and can add an Admin role to more users, if needed.


 - <strong>Profile</strong>

      - users can view their profile (username, registered email, roles)
      - users can edit their username, email or change their password
      - users can add an image to their profile


 - <strong>Language</strong>
    - there is an option to change the language (English - Bulgarian)
    - only the Navigation bar is translated (to be continued)


# <p id="future-features">Future Features</p>
  - Followers
    - follow user
    - unfollow user
    - block user
    - unblock user
    

  - Design
    - Improve the design on all pages


  - Multiple item lists
    - add an option to have more shopping lists and order them in categories


  - More options on My Collection page
    - add categories and filters that can enable users to order their collections


  - Language
    - translate all pages into Bulgarian
   


# <p id="project-structure">Project Structure</p>
- Server 

  - **config** - files used to configure the application
  - **exceptions** - files containing custom exceptions
  - **model** - all database models used in the application
  - **repository** - files directly accessing mySQL database and manipulating data
  - **service** - all business logic used in the application
  - **validation** - files containing customs validation logic
  - **web** - controllers used to handle client requests and make calls to the service layer


# <p id="email">Emails</p>
  - Users receive emails after sucessful registration in the app:

![Email after Registration](https://user-images.githubusercontent.com/104612887/228738463-5d037bab-24e5-4d45-80b6-49b380ef9323.png)

  - Admins receive daily emails if there are new added comments that are still not approved:

![Email for Admin approval](https://user-images.githubusercontent.com/104612887/228433003-8704df09-019b-450b-b66d-119977ae50c0.png)


# <p id="application-pictures">Application Pictures</p>

## Desktop
![Index](https://user-images.githubusercontent.com/104612887/227734581-d495bb90-0d5b-4c86-a2c9-1af4e096daf8.png)

![Footer](https://user-images.githubusercontent.com/104612887/227734614-cecf06e1-f8fc-43f8-8068-8cb2adcf9f50.png)

![Recipes](https://user-images.githubusercontent.com/104612887/227734891-de6d7e4e-02a7-4fa4-bb64-cec1d40ac4c8.png)

![Product list](https://user-images.githubusercontent.com/104612887/227734789-1397b30e-8212-43fd-bca9-e2464b1d3107.png)

![Profile](https://user-images.githubusercontent.com/104612887/227734987-a902787f-0dca-4ccf-becf-4c3928338460.png)

![Recipe details](https://user-images.githubusercontent.com/104612887/227735109-15467118-142a-401c-8568-66220edf34da.png)

![Comments](https://user-images.githubusercontent.com/104612887/227735189-90c69cca-95ba-4711-bfbd-a72e8e888624.png)


## Mobile



