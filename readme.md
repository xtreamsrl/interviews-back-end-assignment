
  
<!---
Hi! We're happy you opened this file, not everyone does!
To let us know you did, paste a capybara picture 
in the How to Run section 😊 
-->

# Backend Engineer Interview Assignment

## Introduction

This is an interview exercise for the Digital Products team of [xtream](https://www.linkedin.com/company/xtream-srl). In the following sections, you will find a number of challenges that we ask you to  implement. You **DO NOT NECESSARILY need to complete 100% of them**, but rather only the ones you feel comfortable with.

:watch: We estimate it should take around 8 hours to solve the challenges, and we give you **1 week** to submit a solution, so that you can do it at your own pace.

### Deliverables

Simply fork this repository and work on it as if you were working on a real-world project assigned to you. A week from now, we will checkout your work and evaluate it. The fork can also be private, we will just share our accounts with you so you can grant read access.

:heavy_exclamation_mark: **Important**: At the end of this README, you will find a "How to run" section that is not written out. Please, write there instructions on how to run your code.    


### Evaluation

Your work will be assessed according to several criteria. As an example, these include:

* Code quality
* Design Patterns
* Project Structure
* Work quality (commits, branches, workflow, tests, ...)
* Provided Documentation

#### A Friendly Reminder:
We're all for AI advancements and cool tech (we wouldn't be here otherwise, right?), but let's not outsource our fun to our silicon-based friends! We're really keen on getting to know your approach, your way of thinking and, of course, your wonderful mistakes along the way! So, while we totally get the allure of asking ChatGPT (or its cousins) to whip up a solution, we'd like this party to be strictly humans-only. Remember, it's the quirks, the head-scratching moments, and those glorious "Eureka!" instants that make us love this job. So go ahead, get your hands keyboard-deep in code, and show us what you got! Happy coding! 🎉💻🚀

### Let's get started

We do understand that some topics might be unfamiliar for you. Therefore, pick any number of challenges and try to complete them.

:heavy_exclamation_mark:**Important**: you might feel like the tasks are somehow too broad, or the requirements are not fully elicited. **This is done on purpose**: we want to give you the freedom to make your own choices and to put as fewer constraints as possible on your work. We appreciate if you could record any decisions, assumptions and doubts, together with any questions that you will ask in a real-world scenario. If you want to choose our stack instead, we generally work with TypeScript and NestJS.

---   

### Problem Domain

Your task is to build the backend for **FreshCart Market**, a simple grocery e-commerce website, where you can search for products, add to a cart, and pay for the products. The store has also a membership reward program: you gather points based on the amount spent and you can use them to get discounts. We need to focus on the customer part instead of the admin part that handles product list and available quantity. This part can be directly manipulated in the database. Do not consider authentication or sign in, imagine if the user that is interacting with the system is always the same.

#### Challenge #1

Design an API to get the list of products available. Each product should have a name, an image, a price, the available quantity, and a category. The API is directly used in the Frontend of FreshCart Market, so consider the possible heavy load that receiving the whole list can generate and find a solution

#### Challenge #2

The website can be also explored by category, so it has a left panel where the user can see all the categories and the number of products available in that category. When you click on the category the user can see all the products of that category. In the UI there is also an input that can be used to search for a specific product. 

#### Challenge #3

For simplicity, the order and pay API receives all the products and the quantity together with the credit card. An external service must be used to get the money from the user. If the user has enough money the order is placed. 

#### Challenge #4

For every euro spent the user receives 1 point in the membership, and you can exchange 25 points for 1 euro of discount. Update the order placement API to update points on every placed order and check if the user wants to use its points to get a discount while paying. Moreover, there are special products that if present in the order, increase the amount of points assigned by a quantity specified in the product catalog itself.

#### Challange #5
Every grocery has some temporary discounts. FreshCart Market needs to consider that the administrator will insert in a table a list of products together with a percentage discount that will be applied only to a specific date range. Update the system to include this information in the whole process.


## How to run

...
