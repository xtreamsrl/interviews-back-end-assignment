
<!---
Hi! We're happy you opened this file, not everyone does!
To let us know you did, paste a capybara picture 
in the How to Run section 😊 
-->

# React Interview Assignment

## Introduction

This is an interview exercise for the Web & Mobile team of [xtream](https://www.linkedin.com/company/xtream-srl). In the
following sections, you will find a number of challenges that we ask you to implement. You **DO NOT NECESSARILY need to
complete 100% of them**, but rather only the ones you feel comfortable about or that interest you.

:watch: We estimate it should take around 8 hours to solve the challenges, and we give you **1 week** to submit a
solution, so that you can do it at your own pace.

### Deliverables

Simply fork this repository and work on it as if you were working on a real-world project assigned to you. A week from
now, we will checkout your work and evaluate it.

:heavy_exclamation_mark:**Important**: At the end of this README, you will find a "How to run" section that is not
written out. Please, write there instructions on how to run your code.

### Evaluation

Your work will be assessed according to several criteria. As an example, these include:

* Code quality
* Design Patterns
* Project Structure
* Work quality (commits, branches, workflow, tests, ...)
* Provided Documentation

### Let's get started

We do understand that some topics might be unfamiliar for you. Therefore, pick any number of challenges and try to complete them.

:heavy_exclamation_mark:**Important**: you might feel like the tasks are somehow too broad, or the requirements are not
fully elicited. **This is done on purpose**: we want to give you freedom to take your own choices and to put as fewer
constraints as possible on your work.

---   

### Posts application

The final goal is to create an application that shows a post timeline with comments. Some functionalities can be easy but there are some other that are not so basic.

#### Challenge #1

Create a web service that allows to manipulate Posts and Comments. A Post can have a title and a body, while a Comment only contains some text. The application needs to retrieve, create, update and delete posts. The same is true for the comments on each post. Posts and Comments must be retrieved and ordered based on their last update date (for example: the last update date of a post changes whenever a comment is added to it).

#### Challenge #2

Let's consider the case where we have a hugely larger number of posts (~thousands of them). Redesign, if necessary, the API to retrieve them.

#### Challenge #3

Now it's time to add a User section, otherwise you cannot understand who is writing posts and/or comments. Add a simple signup API, a login API, and show the user reference within posts and comments. When someone asks for posts and comments you need to embed the corresponding user info (name, userId, ...) inside each entity. It would also be nice to add a way to retrieve all the posts that a user has written.

#### Challenge #4

This application is great, and people are using so frequently that the number of posts and comments has grown too much! It's becoming difficult to find a post, and so the frontend team asks you to provide a search API so that a user can write some words inside a searchbar and load all the posts that contain those words.

