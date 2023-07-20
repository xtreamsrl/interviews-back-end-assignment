
<!---
Hi! We're happy you opened this file, not everyone does!
To let us know you did, paste a capybara picture 
in the How to Run section 😊 
-->

# Backend Engineer Interview Assignment

## Introduction

This is an interview exercise for the Web & Mobile team of [xtream](https://www.linkedin.com/company/xtream-srl). In the
following sections, you will find a number of challenges that we ask you to implement. You **DO NOT NECESSARILY need to
complete 100% of them**, but rather only the ones you feel comfortable with or that interest you.

:watch: We estimate it should take around 8 hours to solve the challenges, and we give you **1 week** to submit a
solution, so that you can do it at your own pace.

### Deliverables

Simply fork this repository and work on it as if you were working on a real-world project assigned to you. A week from
now, we will checkout your work and evaluate it.

:heavy_exclamation_mark: **Important**: At the end of this README, you will find a "How to run" section that is not
written out. Please, write there instructions on how to run your code.    


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

:heavy_exclamation_mark:     
**Important**: you might feel like the tasks are somehow too broad, or the requirements are not
fully elicited. **This is done on purpose**: we want to give you freedom to take your own choices and to put as fewer
constraints as possible on your work. As an example, feel free to choose any technology you like for the database.
---   

### Problem Domain

Your task is to build the backend for an application that handles posts, comments, and user interactions. The end goal is to facilitate a timeline that displays posts and their corresponding comments.

#### Challenge #1

Design a web service that allows to manipulate posts and comments. A post can have a title and a body, while a comment only contains some text. The application needs to provide functionalities to retrieve, create, update, and delete posts and their corresponding comments. Posts and comments must be retrieved and ordered based on their last update date (e.g., the last update date of a post changes whenever a comment is added to it).

#### Challenge #2

Consider a scenario where the application handles a significantly larger number of posts (~thousands). If necessary, redesign the API to efficiently retrieve them.

#### Challenge #3

Introduce user management by adding APIs for user signup and login. Also, display user references within posts and comments. When posts and comments are fetched, relevant user info (name, userId, etc.) should be embedded within each entity. Consider providing a functionality to fetch all posts written by a particular user.

#### Challenge #4

This application is great, and people are using so frequently that the number of posts and comments has grown too much! It's becoming difficult to find a post, and so the frontend team asks you to provide a search API so that a user can write some words inside a searchbar and load all the posts that contain those words. Can you help them?

#### Challenge #5
As the number of users and their interactions grow, we need to consider the database design's efficiency. How would you structure a database to handle the posts, comments, and users? Let's see your prowess in database design. Please also write a small paragraph to explain your choices.

#### Challenge #6
To ensure our application runs smoothly, we need to identify any potential bugs or issues. The solution? Testing, of course! Could you write some tests for your code to help us prevent potential meltdowns? Also, make sure to let us know how to run these tests. And remember, a good test is worth a thousand debugs!


## How to run

...
