package menu;

import model.*;
import persistence.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.regex.Pattern;

public class MusicMenu {

    // Repositories
    static RepositoryUser           repoUser;
    static RepositoryUserRole       repoUserRole;
    static RepositoryArtist         repoArtist;
    static RepositoryAlbum          repoAlbum;
    static RepositorySong           repoSong;

    // User Roles
    static List<UserRole>           userRoles;

    // Utils
    static Scanner                  in;

    // Globals
    static User                     currentUser;        // User who is logged in
    static int                      currentState = 0;   // 0: Logged out
                                                        // 1: Logged in, main menu
                                                        // 2: Profile Menu
                                                        // 3: Music Menu;
                                                        // 4: Content Menu
                                                        // 5: User Menu

    // Constants
    static final Pattern            EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);



    // Menus
    static String []                logInMenu =
            {
                    "User Authentication Menu:",
                    "Exit",
                    "Log In",
                    "Create User"
            };

    static String[]                 userMainMenu =
            {
                    "User Menu:",
                    "Log Out",
                    "My Profile",
                    "My Music",
                    "Edit content",
                    "Admin Actions"
            };

    static String[]                 profileMenu =
            {
                    "Profile Menu:",
                    "Back to User Menu",
                    "Change user name",
                    "Change password",
                    "Change e-mail"
            };

    static String[]                 musicMenu =
            {
                    "Music Menu:",
                    "Back to Main Menu"
            };

    static String[]                 artistOptions =
            {
                    "Artist Options",
                    "Return to artist selection",
                    "Browse Artist Albums",
                    "Edit current artist",
                    "Delete current artist"
            };

    static String []                albumOptions =
            {
                    "Album options",
                    "Return to album selection",
                    "Browse Album Songs",
                    "Edit current album",
                    "Delete current album"
            };

    static String []                songOptions =
            {
                    "Song options",
                    "Return to song selection",
                    "Edit current song",
                    "Delete current song"
            };

    static String []                adminActions =
            {
                    "Admin Actions",
                    "Return to Main Menu",
                    "Display all users",
                    "Set User Role"
            };

    static List<UserRole>           defUserRoles = Arrays.asList(
            new UserRole("Administrator", true, true),
            new UserRole("Contributor", false, true),
            new UserRole("User", false, false),
            new UserRole("Banned", false, false)
    );
    /////// HOW TO TRUNCATE TABLE USING HIBERNATE????



    public static void main(String[] args) {

        System.out.println("Welcome to the music app");
        // Util init
        in = new Scanner(System.in);

        // Load all repositories
        repoUser =      new RepositoryUser();
        repoUserRole =  new RepositoryUserRole();
        repoArtist =    new RepositoryArtist();
        repoAlbum =     new RepositoryAlbum();
        repoSong =      new RepositorySong();

        // Load all user roles
        System.out.println("Loading user roles...");
        userRoles = repoUserRole.listUserRoles();
        if (userRoles == null)
        {
            System.out.println("Failed to retrieve user roles!");
            return;
        }
        System.out.println("Found " + userRoles.size() + " roles");

        if (userRoles.size() == 0)
        {
            System.out.println("No user roles found! Creating from default...");
            for(UserRole userRole: defUserRoles)
            {
                if (!repoUserRole.createUserRole(userRole))
                {
                    System.out.println("Failed to create user role!");
                    return;
                }
            }
            System.out.println("Successfully created user roles!");
        }

        // Find if the app has any admins
        System.out.println("Requesting admin list...");
        UserRole userRole = repoUserRole.getRoleByName("Administrator");
        if (userRole == null)
        {
            System.out.println("Failed to get user role from database!");
            return;
        }
        List<User> adminList = repoUser.listUsersByRole(userRole);       // If no admins found, we have to create one
        if (adminList == null)
        {
            System.out.println("Error reading admin list from data base!");
            return;
        }
        if (adminList.size() == 0)
        {
            System.out.println("No administrator account has been found! We have to create one.");
            currentUser = saveUser("Administrator");
            if (currentUser == null)
                return;

            currentState = 1;
            System.out.println("Welcome, " + currentUser.getUserRole().getRoleName() + " " + currentUser.getUserName());
        }

        while (true)
        {
            switch (currentState)
            {
                case -1:
                    return;
                case 0:         // Auth Menu
                    logIn();
                    break;
                case 1:         // User Menu
                    showUserMenu();
                    break;
                case 2:         // Profile
                    showUserProfile();
                    break;
                case 3:         // Music Menu
                    System.out.println("Not implemented yet!");
                    break;
                case 4:         // Content Menu
                    editContent();
                    currentState = 1;
                    break;
                case 5:         // Admin menu
                    adminMenu();
                    break;
                default:
                    currentState = 0;
                    currentUser = null;
                    System.out.println("Not implemented yet!");
                    break;
            }
        }

    }

    public static void adminMenu() {
        int option = callMenu(adminActions, 0);
        List<User>  userList;
        String[]    userNames;
        User        userToEdit;

        switch (option) {
            case 0:         // Return to main menu
                currentState = 1;
                break;
            case 1:         // Display all users
                userList = repoUser.listAllUsers();
                for (User user: userList)
                {
                    user.displayUserData();
                    System.out.println();
                }
                break;
            case 2:         // Set User Role
                userList = repoUser.listAllUsers();
                if (userList == null)
                {
                    System.out.println("Failed to load user list!");
                    return;
                }
                if (userList.size() == 0)
                {
                    System.out.println("No users found!");
                    return;
                }
                userNames = new String[userList.size() + 2];
                userNames[0] = "Please select a user: ";
                userNames[1] = "Return to Admin Menu";
                for (int i = 0; i < userList.size(); i++)
                {
                    userNames[i + 2] = userList.get(i).getUserName();
                }
                option = callMenu(userNames, 0);
                if (option == 0)
                    break;
                else
                    userToEdit = userList.get(option - 1);

                userNames = new String[userRoles.size() + 2];
                userNames[0] = "Please select a user role to assign to " + userToEdit.getUserName();
                userNames[1] = "Return to Admin menu";
                for (int i = 0; i < userRoles.size(); i++)
                {
                    userNames[i + 2] = userRoles.get(i).getRoleName();
                }
                option = callMenu(userNames, 0);
                if (option == 0)
                    break;

                userToEdit.setUserRole(userRoles.get(option - 1));
                if (repoUser.updateUser(userToEdit))
                    System.out.println("Successfully updated the user " + userToEdit.getUserName());
                else
                    System.out.println("Failed to update the user!");
                break;
        }
    }

    public static void logIn()
    {
        int option = callMenu(logInMenu, 0);

        switch (option) {
            case 0:
                System.out.println("Goodbye!");
                System.exit(0);
            case 1:
                authUser();
                break;
            case 2:
                currentUser = saveUser("User");
                if (currentUser == null)
                    break;
                currentState = 1;
                System.out.println("Welcome, " + currentUser.getUserRole().getRoleName() + " " + currentUser.getUserName());
                break;
        }
    }

    public static void showUserMenu() {
        int option;
        // Check how many rows to hide from user
        String userRole = currentUser.getUserRole().getRoleName();
        if (userRole.equals("Administrator"))
            option = 0;
        else if (userRole.equals("Contributor"))
            option = 1;
        else
            option = 2;

        //System.out.println("Your role in the system is " + userRole);
        option = callMenu(userMainMenu, option);

        if (option == 0) {
            System.out.println("Logging you out. Goodbye!");
            currentUser = null;
            currentState = 0;
            return;
        }

        currentState = option + 1;
    }

    public static void showUserProfile()
    {
        currentUser.displayUserData();

        int option = callMenu(profileMenu, 0);

        switch (option)
        {
            case 0:
                currentState = 1;
                break;

            case 1:
                currentUser.setUserName(changeUsername());
                if (repoUser.updateUser(currentUser))
                    System.out.println("User name changed successfully!");
                else
                    System.out.println("Failed to change the user name!");
                break;

            case 2:
                currentUser.setPasswordHash(changePassword());
                if (repoUser.updateUser(currentUser))
                    System.out.println("Password changed successfully!");
                else
                    System.out.println("Failed to change the password!");
                break;


            case 3:
                currentUser.seteMail(changeEmail());
                if (repoUser.updateUser(currentUser))
                    System.out.println("E-Mail changed successfully!");
                else
                    System.out.println("Failed to change the e-mail!");
                break;
        }
    }

    public static void editContent()
    {
        int state = 1;
        char opt = ' ';
        int option = 0;
        boolean bEdit = false;
        boolean bRes = false;
        String[] contentArray;
        Artist currentArtist = new Artist();
        Album currentAlbum = new Album();
        Song currentSong = new Song();
        String artistName;
        String albumName;

        while (true)
        {
            switch (state) {

                case 1:         // Create a list of artists
                    List<Artist> artistList = repoArtist.listAll();
                    if (artistList == null)
                    {
                        System.out.println("Error retrieving artist list from the database!");
                        return;
                    }
                    if (artistList.size() == 0)
                    {
                        System.out.println("No artists have been found in the database! Creating a new one...");
                        bEdit = false;
                        state = 2;
                        break;
                    }
                    contentArray = new String[artistList.size() + 3];
                    contentArray[0] = "Please select an artist from the database:";
                    contentArray[1] = "Return to Music menu";
                    contentArray[2] = "Add new Artist";

                    for (int i = 0; i < artistList.size(); i++) {
                        contentArray[i + 3] = artistList.get(i).getArtistName();
                    }

                    option = callMenu(contentArray, 0);
                    if (option == 0)
                        return;
                    else if (option == 1) {
                        bEdit = false;
                        state = 2;
                    }
                    else {
                        currentArtist = artistList.get(option - 2);
                        state = 101;
                    }
                    break;


                case 101:   // Artist options
                    // Show artist data and let user decide what to do
                    System.out.println("Artist name:      " + currentArtist.getArtistName());
                    System.out.println("Artist is active: " + currentArtist.isActive());
                    option = callMenu(artistOptions, 0);

                    switch (option)
                    {
                        case 0:     // Return to artist selection
                            state = 1;
                            break;
                        case 1:     // Browse artist albums
                            state = 11;
                            break;
                        case 2:     // Edit artist
                            state = 2;
                            bEdit = true;
                            break;
                        case 3:     // Delete artist
                            System.out.println("Not implemented yet!");
                            break;
                    }
                    break;

                case 2: // Create a new artist
                    // Enter the artist name
//                    while (in.hasNext())
//                        in.nextLine();
                    in = new Scanner(System.in);
                    System.out.print("Please enter the Artist name, or type ##Q to quit: ");
                    artistName = in.nextLine();

                    // If artist name = '##Q' - quit
                    if (artistName.equals("##Q") || artistName.equals("##q")) {
                        return;
                    }

                    if (!validateName(artistName, 3, 50)) {
                        System.out.println("Artist name should be between 3 and 50 characters and only contain letters!");
                        break;
                    }

                    // Check if the artist already exists
                    if (repoArtist.findByName(artistName) != null) {
                        System.out.println("Artist with this name already exists!");
                        break;
                    }
                    if (!bEdit)
                        currentArtist = new Artist();
                    currentArtist.setArtistName(artistName);

                    System.out.print("Please specify if the artist is still active (y/n): ");
                    opt = in.next().charAt(0);
                    currentArtist.setActive(opt == 'y' || opt == 'Y');

                    // Create or edit the artist
                    if (bEdit)
                        bRes = repoArtist.update(currentArtist);
                    else
                        bRes = repoArtist.create(currentArtist);
                    if (!bRes) {
                        System.out.println("Failed to create or update the artist!");
                        return;
                    }


                    // Check if the user wants to add an album
                    System.out.print("Do you want to add a new album to this artist? (y/n): ");
                    opt = in.next().charAt(0);
                    if (opt == 'y' || opt == 'Y')
                        state = 11;
                    else
                        state = 1;
                    break;

                case 11: // Create the list of albums
                    List<Album> albumList = repoAlbum.listByArtist(currentArtist);
                    if (albumList == null)
                    {
                        System.out.println("Error retrieving album list from the database!");
                        return;
                    }
                    if (albumList.size() == 0)
                    {
                        System.out.println("No albums for this artist have been found! Creating a new one...");
                        state = 12;
                        break;
                    }
                    contentArray = new String[albumList.size() + 3];
                    contentArray[0] = "Please select an album from the database:";
                    contentArray[1] = "Return to Artist menu";
                    contentArray[2] = "Add new Album";

                    for (int i = 0; i < albumList.size(); i++) {
                        contentArray[i + 3] = albumList.get(i).getAlbumName();
                    }

                    option = callMenu(contentArray, 0);
                    if (option == 0)
                        state = 1;
                    else if (option == 1) {
                        bEdit = false;
                        state = 12;
                    }
                    else {
                        currentAlbum = albumList.get(option - 2);
                        state = 111;
                    }
                    break;

                case 111:       // Album Options
                    System.out.println("Album name:   " + currentAlbum.getAlbumName());
                    System.out.println("Album artist: " + currentArtist.getArtistName());
                    System.out.println("Album genre:  " + currentAlbum.getAlbumGenre());
                    System.out.println("Album year:   " + currentAlbum.getAlbumYear());
                    option = callMenu(albumOptions, 0);

                    switch (option)
                    {
                        case 0:     // Return to album selection
                            state = 11;
                            break;
                        case 1:     // Browse album songs
                            state = 21;
                            break;
                        case 2:     // Edit album
                            state = 12;
                            bEdit = true;
                            break;
                        case 3:     // Delete album
                            System.out.println("Not implemented yet!");
                            break;
                    }
                    break;


                case 12:     // Create a new album - enter name
                    // Enter the album name
//                    while (in.hasNext())
//                        in.nextLine();
                    in = new Scanner(System.in);
                    System.out.print("Please enter the Album name, or type ##Q to quit: ");
                    albumName = in.nextLine();

                    // If album name = '##Q' - quit
                    if (albumName.equals("##Q") || albumName.equals("##q")) {
                        state = 1;
                        break;
                    }

                    // Validate album name
                    if (!validateName(albumName, 3, 50)) {
                        System.out.println("Album name should be between 3 and 50 characters and only contain letters!");
                        break;
                    }

                    // Check if the album already exists
                    if (repoAlbum.findByName(albumName) != null) {
                        System.out.println("Album with this name already exists!");
                        break;
                    }

                    // Create new album
                    if (!bEdit)
                        currentAlbum = new Album();
                    currentAlbum.setAlbumName(albumName);
                    state = 13;
                    break;

                case 13:        // Create new album - enter genre
                    // Set genre
//                    while (in.hasNext())
//                        in.nextLine();
                    in = new Scanner(System.in);
                    System.out.print("Please enter the album genre: ");
                    String genre = in.nextLine();
                    if (!validateName(genre, 3, 50))
                    {
                        System.out.println("Genre name should be between 3 and 50 characters and only contain letters!");
                        break;
                    }
                    currentAlbum.setAlbumGenre(genre);
                    state = 14;
                    break;

                case 14:        // Create new album - enter year
                    System.out.print("Please enter the album year: ");
                    // Check if number has been entered
                    int year;
                    try {
                        year = in.nextInt();
                    }
                    catch (InputMismatchException e)
                    {
                        System.out.println("Year should only contain numbers!");
                        break;
                    }

                    // Check if year is real
                    if (year < 0 || year > Calendar.getInstance().get(Calendar.YEAR))
                    {
                        System.out.println("Year should be between 0 and " + Calendar.getInstance().get(Calendar.YEAR));
                        break;
                    }

                    // Set year
                    currentAlbum.setAlbumYear(year);
                    state = 15;
                    break;

                case 15:    // Create album
                    currentAlbum.setArtist(currentArtist);
                    if (bEdit)
                        bRes = repoAlbum.update(currentAlbum);
                    else
                        bRes = repoAlbum.create(currentAlbum);

                    if (!bRes)
                    {
                        System.out.println("Failed to create/update an album!");
                        return;
                    }

                    // Check if the user wants to add an album
                    System.out.print("Do you want to add a new song to this album? (y/n): ");
                    opt = in.next().charAt(0);
                    if (opt == 'y' || opt == 'Y')
                        state = 21;
                    else
                        state = 11;
                    break;

                case 21: // Create the list of songs
                    List<Song> songList = repoSong.listByAlbum(currentAlbum);
                    if (songList == null)
                    {
                        System.out.println("Error retrieving song list from the database!");
                        return;
                    }
                    if (songList.size() == 0)
                    {
                        System.out.println("No songs for this album have been found! Creating a new one...");
                        state = 22;
                        break;
                    }
                    contentArray = new String[songList.size() + 3];
                    contentArray[0] = "Please select a song from the database:";
                    contentArray[1] = "Return to Albums menu";
                    contentArray[2] = "Add new Song";

                    for (int i = 0; i < songList.size(); i++) {
                        contentArray[i + 3] = songList.get(i).getSongName();
                    }

                    option = callMenu(contentArray, 0);
                    if (option == 0)
                        state = 11;
                    else if (option == 1) {
                        bEdit = false;
                        state = 22;
                    }
                    else {
                        currentSong = songList.get(option - 2);
                        state = 121;
                    }
                    break;

                case 121:       // Album Options
                    System.out.println("Song name:   " + currentSong.getSongName());
                    System.out.println("Song album:  " + currentAlbum.getAlbumName());
                    System.out.println("Song artist: " + currentArtist.getArtistName());
                    System.out.println("Song rating: " + currentSong.getSongRating());
                    System.out.println("Song path:   " + currentSong.getSongPath());
                    option = callMenu(songOptions, 0);

                    switch (option)
                    {
                        case 0:     // Return to song selection
                            state = 21;
                            break;
                        case 1:     // Edit song
                            state = 22;
                            bEdit = true;
                            break;
                        case 2:     // Delete song
                            System.out.println("Not implemented yet!");
                            break;
                    }
                    break;

                case 22:     // Create a new song - enter name
                    // Enter the song name
//                    while (in.hasNext())
//                        in.nextLine();
                    in = new Scanner(System.in);
                    System.out.print("Please enter the Song name, or type ##Q to quit: ");
                    String songName = in.nextLine();

                    // If song name = '##Q' - quit
                    if (songName.equals("##Q") || songName.equals("##q")) {
                        state = 11;
                        break;
                    }

                    // Validate song name
                    if (!validateName(songName, 3, 50)) {
                        System.out.println("Song name should be between 3 and 50 characters and only contain letters!");
                        break;
                    }

                    // Check if the song already exists
                    if (repoAlbum.findByName(songName) != null) {
                        System.out.println("Song with this name already exists!");
                        break;
                    }

                    // Create new song
                    if (!bEdit)
                        currentSong = new Song();
                    currentSong.setSongName(songName);
                    currentSong.setSongYear(currentAlbum.getAlbumYear());
                    state = 23;
                    break;

                case 23:        // Create a new song - enter rating
                    int iRating;
                    System.out.print("Please enter the song rating: ");
                    try {
                        iRating = in.nextInt();
                    }
                    catch (InputMismatchException e)
                    {
                        System.out.println("Rating should be an integer number!");
                        break;
                    }

                    if (iRating < 0 || iRating > 10)
                    {
                        System.out.println("Rating should be a number between 0 and 10!");
                        break;
                    }

                    currentSong.setSongRating(iRating);
                    state = 24;
                    break;

                case 24:        // Create a new song - enter path
                    System.out.print("Please enter the path to the song\n>: ");
//                    while (in.hasNext())
//                        in.nextLine();
                    in = new Scanner(System.in);
                    String path = in.nextLine();
                    if (path.length() > 255) {
                        System.out.println("Path length should not exceed 255!");
                        break;
                    }

                    currentSong.setSongPath(path);
                    state = 25;
                    break;

                case 25:        // Create song
                    currentSong.setAlbum(currentAlbum);

                    if (bEdit)
                        bRes = repoSong.create(currentSong);
                    else
                        bRes = repoSong.update(currentSong);

                    if (!bRes)
                    {
                        System.out.println("Failed to create/update the song!");
                        return;
                    }

                    // Check if the user wants to add another song
                    System.out.print("Do you want to add a new song to this album? (y/n): ");
                    opt = in.next().charAt(0);
                    if (opt == 'y' || opt == 'Y') {
                        bEdit = false;
                        state = 22;
                    }
                    else
                        state = 21;
                    break;

                default:
                    return;
            }
        }

    }

    public static void authUser()
    {
        System.out.println("Authentication...");
        System.out.print("Please enter your user name: ");
        String userName = in.next();
        User user = repoUser.findUserByName(userName);
        if (user == null)
        {
            System.out.println("User " + userName + " does not exist!");
            return;
        }

        if (user.getUserRole().getRoleName().equals("Banned"))
        {
            System.out.println("Your account has been deactivated. Please contact the administrator");
            return;
        }

        System.out.print("Please enter your password: ");
        String password = in.next();
        if (!user.authUser(password))
        {
            System.out.println("Password is incorrect!");
            return;
        }

        currentUser = user;
        currentState = 1;
        System.out.println("Welcome, " + currentUser.getUserRole().getRoleName() + " " + currentUser.getUserName());
    }

    public static User saveUser(String userRoleName)
    {
        // Create user name
        String userName = changeUsername();

        // Enter password
        String password = changePassword();

        // Enter e-mail
        String eMail = changeEmail();

        // Create user role ID
        UserRole userRole = repoUserRole.getRoleByName(userRoleName);
        if (userRole == null)
        {
            System.out.println("Error retrieving user role!");
            return null;
        }

        // Create user
        User user = new User(userName, password, eMail, userRole);
        if (repoUser.createUser(user))
            return user;

        System.out.println("Error connecting to the database!");
        return null;
    }

    public static String changeUsername()
    {
        String userName;
        do {
            System.out.print("Please enter the user name: ");
            userName = in.next();
            if ((userName.length() < 3) || (userName.length() > 20))
            {
                System.out.println("User name should be between 3 and 20 characters!");
                continue;
            }
            boolean userExists = (repoUser.findUserByName(userName) != null);
            if (userExists)
                System.out.println("User name taken! Please choose a different one!");
            else
                break;
        } while (true);
        return userName;
    }

    public static String changePassword()
    {
        String password;
        do {
            System.out.print("Please enter the new password: ");
            password = in.next();
            if ((password.length() < 3) || (password.length() > 20))
            {
                System.out.println("Password should be between 3 and 20 characters!");
                continue;
            }
            break;
        } while (true);
        return password;
    }

    public static String changeEmail()
    {
        String eMail;
        do {
            System.out.print("Please enter the new e-mail: ");
            eMail = in.next();
            if (!EMAIL_PATTERN.matcher(eMail).find())
            {
                System.out.println("Invalid e-mail format!");
                continue;
            }
            if (eMail.length() > 50)
            {
                System.out.println("E-mail cannot be longer than 50 characters!");
                continue;
            }
            break;
        } while (true);
        return eMail;
    }


    public static int callMenu(String[] menu, int hiddenPoints)
    {
        int option;
        do {
            System.out.println("\n");
            System.out.println(menu[0]);
            for (int i = 1; i < menu.length - hiddenPoints; i++)
            {
                System.out.printf("[%d] - %s\n", i - 1, menu[i]);
            }
            System.out.print("Please enter your option: ");
            option = -1;
            try {
                option = in.nextInt();
            }
            catch (InputMismatchException e)
            {
                in.next();
            }

            if ((option < 0) || (option > menu.length - hiddenPoints - 2))
                System.out.println("Incorrect entry! Please enter the correct option!");
            else
                break;

        } while (true);

        return option;
    }

    public static boolean validateName(String name, int min, int max)
    {
        if (name.length() < min || name.length() > max)
            return false;

        Pattern pattern = Pattern.compile("[^a-zA-Z ]");
        return !pattern.matcher(name).find();
    }

}

