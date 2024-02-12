
  
<!---
Hi! We're happy you opened this file, not everyone does!
To let us know you did, paste a capybara picture 
in the How to Run section 😊 
These will be extra points for you!
-->

# Backend Engineer Interview Assignment

## Introduction

This is an interview exercise for the Digital Products team of [xtream](https://www.linkedin.com/company/xtream-srl). In the following sections, you will find a number of challenges that we ask you to implement. You **DO NOT NECESSARILY need to complete 100% of them**: you can choose to complete as many as you want.

:watch: We give you **1 week** to submit a solution, so that you can do it at your own pace. We are aware that you might have other commitments, so we are not expecting you to work on this full-time. You will be evaluated based on the quality of your work, not on the time you spent on it.

### Deliverables

Simply fork this repository and work on it as if you were working on a real-world project assigned to you. A week from now, we will assess your work.

:heavy_exclamation_mark: **Important**: At the end of this README, you will find a "How to run" section that is not written out. Please, write there instructions on how to run your code: we will use this section to evaluate your work.


### Evaluation

Your work will be assessed according to several criteria. As an example, these include:

* Code quality
* Design Patterns
* Project Structure
* Work quality (commits, branches, workflow, tests, ...)
* Provided Documentation

#### A Friendly Reminder:
We’re all about embracing the latest in AI, including GPT and similar technologies. They’re great tools that can provide a helping hand, whether it’s for generating ideas, debugging, or refining solutions. However, for this coding challenge, we’re really keen to see your personal touch. We're interested in your thought process, decision-making, and the solutions you come up with.

Remember, while using AI tools can be incredibly helpful, the essence of this task is to showcase your skills and creativity. Plus, be prepared to dive into the details of your code during the technical interview. Understanding the 'why' and 'how' behind your decisions is crucial, as it reflects your ability to critically engage with the technology you're using.

So, feel free to lean on AI for support, but ensure your work remains distinctly yours. We're looking for a blend of technical savvy and individual flair. Dive in, get creative, and let’s see what you can create. Excited to see your work. Happy coding! 🚀💼👩‍💻

### Let's get started

We do understand that some topics might be unfamiliar for you. Therefore, pick any number of challenges and try to complete them.

:heavy_exclamation_mark:**Important**: you might feel like the tasks are somehow too broad, or the requirements are not fully elicited. **This is done on purpose**: we want to give you the freedom to make your own choices and to put as fewer constraints as possible on your work. We appreciate if you could record any decisions, assumptions and doubts, together with any questions that you will ask in a real-world scenario. If you want to choose our stack instead, we generally work with TypeScript and NestJS.

---   

### Problem Domain

Your task is to build the backend for **FreshCart Market**, a simple grocery e-commerce website, where you can search for products, add to a cart, and pay for the products.   

The store also has a membership reward program: based on what you spend, you get points that you can use to get discounts.   

For the sake of this assignment, let's focus on creating the **customer** part instead of the **admin** part ( which handles all the products and their available quantities): the **admin** part can be directly manipulated in the database.   

Do not consider authentication,sign in or multiple users: for simplicity, imagine the user that is interacting with the system is always the same.

#### Challenge #1: Available Products

Design an API to get the list of the available products. Each product should have a name, an image, a price and the available quantity, and a category.   
The frontend of FreshCart Market (which you don't need to develop) will use this API directly: consider the possible heavy load that receiving a large list of product can generate and propose a solution.

#### Challenge #2: Categories and Search

The FreshCart website can be also explored by category: there is a left panel where the user can see all the categories and the number of products available in that category.  
When users click on the category, they can see all the products for that category. There is also an input that can be used to search for a specific product. 
Design an API (or multiple APIs) to allow these features.

#### Challenge #3: Order & Payment

It's time to implement the order and payment part. The user can add products to the cart and then place the order. The order should contain the list of products and the quantity.   
For simplicity, the API receives all this info together with the credit card details. If the user has enough money, the API will return a success message, otherwise, it will return an error message.
An external service must be used to get the money from the user: you can find the swagger documentation in the file `payment-service.yaml`.

#### Challenge #4: Reward Program

For every euro spent, the user receives 1 reward point. 25 points equals 1 euro discount.   
Update the order placement API (Challenge #3) to update points on every placed order.  
Also update such API to allow the user to use the points to get a discount on the order.  
Moreover, there are some special products that increase the amount of points earned: such property must be set in the product catalog.

#### Challenge #5: Discounts
Every grocery store has some temporary discounts. FreshCart Market needs to consider that the administrator will insert in a table a list of products together with a percentage discount that will be valid only for a specific date range.   
Update the system to include this information in the whole process.

## How to run

...
