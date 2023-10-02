# Book-List-Application

<p>This repository contains a shelf application developed using modern technologies. Below, you will find an overview of the technology stack used and the functionality of the application.</p>
<h2>Technology Used</h2>
    <ul>
        <li>Jetpack Compose</li>
        <li>Room Database</li>
        <li>Kotlin</li>
        <li>MVVM (Model-View-ViewModel) Architecture</li>
    </ul>

   <h2>Application Functionality</h2>
    <p>The application offers the following features:</p>
    <ol>
        <li><strong>Authentication Mechanism:</strong> Users are required to authenticate using their name, which serves as the primary key in the database. New users are signed in if their name is not found in the database.</li>
        <li><strong>Login:</strong> Returning users can log in by providing their previously registered details. If a user enters incorrect details (e.g., password or country), a toast message prompts them to enter the correct information.</li>
        <li><strong>Book Listing:</strong> Upon successful login, users can view a list of books. They have the option to sort the list using various filters, such as ascending/descending order, hits for books, book names, etc.</li>
        <li><strong>Favorites:</strong> Users can mark or unmark books as favorites using the favorite icon button either on the listing page or inside the book description page. These favorites are saved internally in the database.</li>
        <li><strong>Persistent Data:</strong> All user interactions, including sorting and marking books as favorites, are stored in the database. This data is retained even when the user logs out and logs in again, allowing them to see their previous actions.</li>
    </ol>

  <p>This application leverages modern Android development practices and libraries, making it a powerful and user-friendly bookshelf management tool.</p>
