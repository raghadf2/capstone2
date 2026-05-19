SpaceFlow - Space Booking System

Overview

SpaceFlow is a Spring Boot-based backend system for booking spaces such as meeting rooms, study rooms, and event spaces.
It provides users with the ability to search, book, review, and manage spaces efficiently with advanced business logic like waiting lists, availability tracking, payments, and notifications.

Features

User Management:

Userlogin
Role-based access control (USER / OWNER / ADMIN)


Space Management:

Create, update, delete spaces (OWNER only)
Filter spaces by city, price, capacity
View top-rated & most booked spaces
Recommendation system based on user history


Booking System:

Create, update, cancel bookings
Prevent time conflicts (double booking protection)
Recurring bookings
Booking rescheduling
Price estimation based on duration


Payment System:

Booking payment confirmation
Status update after payment


Reviews & Ratings:

Add/update/delete reviews
One review per user per space
Restriction: only users with completed bookings can review


Favorites:

Add/remove favorite spaces
View user favorite spaces


Availability System:

Define working hours for spaces
Check available time slots
Smart slot calculation


Waiting List System:

Join waiting list when space is unavailable
Auto-positioning in queue
Notification system when space becomes available


Notifications:

Email notifications
WhatsApp notifications (UltraMsg API)


Business Logic Highlights:

Prevent overlapping bookings
Role-based authorization
Waiting list prioritization
Smart recommendation engine
Cancellation restrictions (policy-based)
Review eligibility rules
