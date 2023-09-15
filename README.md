# Daily Tarot

A mobile tarot reading application designed to offer users an intuitive and enhanced Tarot card reading experience without the need for a physical deck or in-depth Tarot knowledge.


## Summary

Replicate the experience of a traditional tarot reading through a mobile application. Users are prompted to sign in, after which they are granted one free reading per day. Additional readings can be availed by watching a reward ad video. Each reading consists of 3 cards drawn at random, accompanied by their interpretations.

## Problem Statement

Traditional Tarot card readings necessitate a physical deck and a knowledgeable interpreter. This application seeks to overcome these barriers by digitizing the tarot reading process, making it widely accessible. Future implementations may involve AI integrations to provide novel interpretations of card combinations.

## Features

### User Authentication:
- **Sign Up**: Create a new account with your email.
- **Verification**: Verify your email by entering the code sent to your inbox.
- **Login**: Access the app's features by logging in.

### Tarot Readings:
- **3 Card Draw Reading**: Engage with a 3-card draw reading to understand your past, present, and future.
- **All Cards View**: Browse through all the Tarot cards in the deck.
- **Card Details**: Click on any card's image to view a detailed, enlarged image alongside its interpretation.
- **Past Readings**: Visit your reading history, displaying the date, the three card images, and their orientation.

### User Interface & Experience:
- **Themes**: Choose between light and dark themes using shared preferences.
- **Glide Integration**: Enjoy a smooth scrolling experience when browsing Tarot card images.

## Minimum Viable Product (MVP)

- **User Interface (UI)**: Simple and user-friendly design enabling users to shuffle and draw cards.
- **Reading Display**: Highlight the three chosen cards along with their descriptions.
- **User Profiles**: Offer users a single complimentary reading daily post-login. Additional readings can be obtained via reward ads. Stretch goal includes accessing previous readings.
- **Tarot Index**: Feature allowing users to delve into the stored meanings of each Tarot card.

## Team Members

- Aaron Clark
- Alex Chao
- Dylan Cooper
- Jon Stillson

## Tools & Libraries

- **AWS Amplify**: Cloud integration.
- **AWS Cognito**: User authentication.
- **SQLite**: Local storage solution for Tarot card objects.
- **DynamoDB**: Storing user readings.
- **AdMobs**: User can watch a reward ad to do another drawing.
- **Glide**: For smooth image loading in RecyclerViews.
- **Shared Preferences**: For theme settings.

## Contributing

For suggestions, bug reports, or feature requests, please open an issue through our GitHub repository.
