package menu;

import model.Artist;
import model.User;
import model.UserRole;
import persistence.RepositoryArtist;
import persistence.RepositoryUser;
import persistence.RepositoryUserRole;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MusicMenu {

    // Repositories
    static RepositoryUser           repoUser;
    static RepositoryUserRole       repoUserRole;
    static RepositoryArtist         repoArtist;

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
                    "Content Actions",
                    "User Actions"
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

    static String[]                 contentMenu =
            {
                    "Content Menu:",
                    "Back to Main Menu",
                    "Add Artist",
                    "Add Album",
                    "Add Song"
            };



    public static void main(String[] args) {

        System.out.println("Welcome to the music app");
        // Util init
        in = new Scanner(System.in);

        // Load all repositories
        repoUser =      new RepositoryUser();
        repoUserRole =  new RepositoryUserRole();

        // Load all user roles
        System.out.println("Loading user roles...");
        userRoles = repoUserRole.listUserRoles();
        if (userRoles == null)
        {
            System.out.println("User roles not found!");
            return;
        }
        System.out.println("Found " + userRoles.size() + " roles");

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
                    showContentMenu();
                    break;
                default:
                    currentState = 0;
                    currentUser = null;
                    System.out.println("Not implemented yet!");
                    break;
            }
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
        System.out.println("User name: " + currentUser.getUserName());
        System.out.println("Role:      " + currentUser.getUserRole().getRoleName());
        System.out.println("E-mail:    " + currentUser.geteMail());


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

    public static void showContentMenu()
    {
        int option = callMenu(contentMenu, 0);

        switch (option)
        {
            case 0:
                currentState = 1;
                break;
            case 1:

        }
    }

//    public static void editContent(int content) // 1 - artist, 2 - album, 3 - song
//    {
//        int state = 1;
//        String artistName = "";
//        String albumName = "";
//
//        while (state > 0)
//        {
//            switch (state) {
//
//                case 1:         // Check if we need to create a list of artists
//                    if (content == 1)
//                        state = 3;
//                    else
//                        state = 2;
//                    break;
//
//                case 2:         // List all the existing artists
//                    if ()
//
//                case 1: // Enter the artist
//                    // Enter the artist name
//                    do {
//                        System.out.print("Please enter the Artist name, or type #Q to quit: ");
//                        artistName = in.next();
//                        if (artistName.length() > 50) {
//                            System.out.println("Artist name should be less than 50 characters!");
//                            continue;
//                        }
//                        break;
//                    } while (true);
//
//                    // If artist name = 'Q' - quit
//                    if (artistName.equals("#Q") || artistName.equals("#q")) {
//                        state = 0;
//                        break;
//                    }
//
//                    // Check if we need to create one
//                    if (repoArtist.findByName(artistName) == null) {
//                        // New artist needed
//                        state = 2;
//                        if (content > 1) {
//                            System.out.print("Artist not found. Create a new one? (y/n): ");
//                            char opt = in.next().charAt(0);
//                            if (opt != 'y' && opt != 'Y')
//                                state = 1;
//                        }
//                        break;
//                    }
//
//                    // New artist not needed
//                    if (content == 1)
//                    {
//                        // If we wanted to create an artist, we don't need to do anything else.
//                        System.out.println("Artist with this name already exists!");
//                        state = 0;
//                        break;
//                    }
//                    state = 3;
//                    break;
//
//                case 2: // Create a new artist
//                    Artist artist = new Artist();
//                    artist.setArtistName(artistName);
//
//                    // Set genre
//                    artist.setArtistGenre("Generic");
//
//                    System.out.print("Please specify if the artist is still active: ");
//                    char opt = in.next().charAt(0);
//                    artist.setActive(opt == 'y' || opt == 'Y');
//
//                    if (repoArtist.create(artist))
//                        System.out.println("Artist created successfully!");
//                    else
//                        System.out.println("Failed to create the artist!");
//
//                    if (content == 1)
//                        state = 0;
//                    else
//                        state = 3;
//                    break;
//
//                case 3: // Enter the album name
//                    do {
//                        System.out.print("Please enter the Album name, or type ##Q to quit: ");
//                        albumName = in.next();
//                        if ((albumName.length() < 3) || (albumName.length() > 50)) {
//                            System.out.println("Album name should be less than 50 characters!");
//                            continue;
//                        }
//                        break;
//                    } while (true);
//
//                    // If artist name = 'Q' - quit
//                    if (albumName.equals("##Q") || albumName.equals("##q")) {
//                        state = 1;
//                        break;
//                    }
//
//                    // Check if we need to create one
//                    if (repoArtist.findByName(albumName) == null) {
//                        // New artist needed
//                        state = 2;
//                        if (content > 1) {
//                            System.out.print("Artist not found. Create a new one? (y/n): ");
//                            opt = in.next().charAt(0);
//                            if (opt != 'y' && opt != 'Y')
//                                state = 1;
//                        }
//                        break;
//                    }
//            }
//        }
//
//
//    }

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
            System.out.println("\n\n");
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

}

