package com.example.project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tema1 {
    private static String finalMessageOk(String message) {
        return ("{ 'status' : 'ok', 'message' : '" + message + "'}");
    }

    private static String finalMessageError(String message) {
        return ("{ 'status' : 'error', 'message' : '" + message + "'}");
    }

    private static boolean authenticated(String user, String pass) {
        if ((user != null && user.indexOf("-u") != 0) || (pass != null && pass.indexOf("-p") != 0)) {
            System.out.println(finalMessageError("You need to be authenticated"));
            return false;
        }
        return true;
    }

    private static boolean login(String user, String pass) {
        if (!authenticated(user, pass)) return false;
        user = user.replace("-u ", "");
        user = user.replace("'", "");
        pass = pass.replace("-p ", "");
        pass = pass.replace("'", "");

        // căutam user-ul dat în Users.csv
        File file = new File("Users.csv");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader("Users.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.equals(user + ',' + pass)) return true;
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        // dacă nu am găsit user-ul
        System.out.println(finalMessageError("Login failed"));
        return false;
    }

    public static void main(final String[] args) {
        if (args == null) {
            System.out.print("Hello world!");
        } else {
            String user = null;
            String password = null;
            boolean found;
            Question question = new Question();
            Quiz quiz = new Quiz();
            int i;
            switch (args[0]) {
                //////////////////////////////////////
                /*      1. Creare utilizator        */
                /////////////////////////////////////
                case "-create-user":
                    if (args.length == 1 || (args[1] != null && args[1].indexOf("-u") != 0)) {
                        System.out.print(finalMessageError("Please provide username"));
                    } else if ((args.length < 3) || (args[2] != null && args[2].indexOf("-p") != 0)) {
                        System.out.println(finalMessageError("Please provide password"));
                    } else {
                        user = args[1].replace("-u ", "");
                        user = user.replace("'", "");
                        password = args[2].replace("-p ", "");
                        password = password.replace("'", "");
                        found = false;
                        File usersFile = new File("Users.csv");

                        // căutam user-ul dat in Users.csv
                        if (!usersFile.exists()) try {
                            usersFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        try (BufferedReader br = new BufferedReader(new FileReader("Users.csv"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.equals(user + ',' + password)) {
                                    System.out.println(finalMessageError("User already exists"));
                                    found = true;
                                    return;
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }

                        // dacă nu l-am găsit, îl adăugăm în Users.csv
                        if (!found) {
                            System.out.println(finalMessageOk("User created successfully"));
                            try (FileWriter fw = new FileWriter("Users.csv", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
                                out.println(user + "," + password);
                            } catch (IOException e) {
                                System.out.println(e);
                            }
                        }
                    }
                    break;

                /////////////////////////////////////
                /*      2. Creare intrebare        */
                ////////////////////////////////////
                case "-create-question":
                    String text;
                    String typeText;
                    boolean single = false;
                    if (args.length < 3) {
                        System.out.println(finalMessageError("You need to be authenticated"));
                    } else if (login(args[1], args[2])) {
                        // verificare text intrebare
                        if (args.length == 3 || (args[3].indexOf("-text") != 0)) {
                            System.out.println(finalMessageError("No question text provided"));
                            return;
                        }
                        text = args[3].replace("-text ", "");
                        text = text.replace("'", "");
                        if (text.equals("")) {
                            System.out.println(finalMessageError("No question text provided"));
                            return;
                        }
                        // verificare tip intrebare
                        typeText = args[4].replace("-type ", "");
                        typeText = typeText.replace("'", "");

                        if (typeText.equals("")) {
                            System.out.println(finalMessageError("No question type provided"));
                            return;
                        } else if (typeText.equals("single")) {
                            single = true;
                        }

                        // verificăm dacă exista deja întrebarea
                        File questionsFile = new File("Questions.csv");
                        if (!questionsFile.exists()) try {
                            questionsFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        try (BufferedReader br = new BufferedReader(new FileReader("Questions.csv"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.contains(text)) {
                                    System.out.println(finalMessageError("Question already exists"));
                                    return;
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        // verificarea numărului de răspunsuri
                        if (args.length == 5) {
                            System.out.println(finalMessageError("No answer provided"));
                            return;
                        } else if (args.length == 7) {
                            System.out.println(finalMessageError("Only one answer provided"));
                            return;
                        } else if (args.length > 15) {
                            System.out.println(finalMessageError("More than 5 answers were submitted"));
                            return;
                        }
                        // analiza răspunsurilor posibile: la final vom avea un tablou de răspunsuri
                        int noAnswers = 0;
                        Answer[] answers = new Answer[5];
                        Answer tempAnswer = new Answer();
                        for (i = 5; i <= args.length - 1; i++) {
                            if (i % 2 == 1) {
                                // -answer-i "..."
                                tempAnswer = new Answer();
                                noAnswers++;
                                String temp = "-answer-" + noAnswers;
                                if (args[i].indexOf(temp) != 0 || args[i].indexOf(temp + "-is-correct") == 0) {
                                    temp = "Answer " + noAnswers + " has no answer description";
                                    System.out.println(finalMessageError(temp));
                                    return;
                                }
                                temp = args[i].replace((temp + " "), "");
                                temp = temp.replace("'", "");
                                if (temp.equals("")) {
                                    temp = "Answer " + noAnswers + " has no answer description";
                                    System.out.println(finalMessageError(temp));
                                    return;
                                } else {
                                    tempAnswer.text = temp;
                                }
                            } else {
                                // answer-i-is-correct 0/1
                                String temp = "-answer-" + noAnswers + "-is-correct";
                                if (args[i].indexOf(temp) != 0) {
                                    temp = "Answer " + noAnswers + " has no answer correct flag";
                                    System.out.println(finalMessageError(temp));
                                    return;
                                }
                                temp = args[i].replace((temp + " "), "");
                                temp = temp.replace("'", "");
                                if (temp.equals("0") || temp.equals(("1"))) {
                                    if (temp.equals("0")) tempAnswer.correct = false;
                                    else tempAnswer.correct = true;
                                    answers[noAnswers - 1] = tempAnswer;
                                } else {
                                    temp = "Answer " + noAnswers + " has no answer correct flag";
                                    System.out.println(finalMessageError(temp));
                                    return;
                                }
                            }
                        }
                        // daca întrebarea este de tipul single, verificam daca avem un singur răspuns corect
                        if (single) {
                            found = false;
                            for (i = 0; i < noAnswers; i++)
                                if (answers[i].correct && found) {
                                    System.out.println(finalMessageError("Single correct answer question has more than one correct answer"));
                                    return;
                                } else if (answers[i].correct && !found) found = true;
                        }
                        // verificam daca toate raspunsurile sunt diferite
                        for (i = 0; i < noAnswers - 1; i++)
                            for (int j = i + 1; j < noAnswers; j++)
                                if ((answers[i].text).equals(answers[j].text)) {
                                    System.out.println(finalMessageError("Same answer provided more than once"));
                                    return;
                                }
                        // salvam intrebarea: text, single/multiple, numar de raspunsuri, raspunsuri
                        // raspunsurile vor fi salvate: text, corect
                        try (FileWriter fw = new FileWriter("Questions.csv", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
                            question = new Question(text, single, noAnswers, answers);
                            out.println(question.toString());
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        System.out.println(finalMessageOk("Question added successfully"));

                    }
                    break;

                ///////////////////////////////////////////
                /*      3. ID intrebare dupa nume        */
                //////////////////////////////////////////
                case "-get-question-id-by-text":
                    if (args.length < 3) {
                        System.out.println(finalMessageError("You need to be authenticated"));
                    } else if (login(args[1], args[2])) {
                        String givenText;
                        givenText = args[3].replace("-text ", "");
                        givenText = givenText.replace("'", "");
                        found = false;
                        File questionsFile = new File("Questions.csv");
                        if (!questionsFile.exists()) try {
                            questionsFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        try (BufferedReader br = new BufferedReader(new FileReader("Questions.csv"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.contains(givenText)) {
                                    found = true;
                                    System.out.println(finalMessageOk(line.split(",")[0]));
                                    return;
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        // not found
                        System.out.println(finalMessageError("Question does not exist"));
                        return;
                    }
                    break;
                //////////////////////////////////////
                /*      4. Afisare intrebari       */
                /////////////////////////////////////
                case "-get-all-questions":
                    if (args.length < 3) {
                        System.out.println(finalMessageError("You need to be authenticated"));
                    } else if (login(args[1], args[2])) {
                        String temp = "[";
                        File questionsFile = new File("Questions.csv");
                        if (!questionsFile.exists()) try {
                            questionsFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        try (BufferedReader br = new BufferedReader(new FileReader("Questions.csv"))) {
                            String line;
                            found = false;
                            while ((line = br.readLine()) != null) {
                                if (found) temp += ", ";
                                else found = true;
                                temp += "{\"question_id\" : \"" + line.split(",")[0] + "\", ";
                                temp += "\"question_name\" : \"" + line.split(",")[1] + "\"}";
                            }
                            temp += "]";
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        System.out.println(finalMessageOk(temp));

                    }
                    break;
                //////////////////////////////////////
                /*      5. Creare chestionar       */
                /////////////////////////////////////
                case "-create-quizz":
                    if (args.length < 3) {
                        System.out.println(finalMessageError("You need to be authenticated"));
                    } else if (login(args[1], args[2])) {
                        // verififcam daca exista deja chestionarul dat
                        String name;
                        name = args[3].replace("-name ", "");
                        name = name.replace("'", "");
                        File quizFile = new File("Quizzes.csv");
                        if (!quizFile.exists()) try {
                            quizFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        try (BufferedReader br = new BufferedReader(new FileReader("Quizzes.csv"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.contains(name)) {
                                    System.out.println(finalMessageError("Quizz name already exists"));
                                    return;
                                } else System.out.println(line);
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        // verificam numarul intrebarilor din chestionar
                        if (args.length > 10 + 4) {
                            System.out.println(finalMessageError("Quizz has more than 10 questions"));
                            return;
                        }
                        // verificam existenta ID-urilor intrebarilor
                        int noQuestions = 0;
                        Question[] questions = new Question[10];
                        File questionFile = new File("Questions.csv");
                        if (!questionFile.exists()) try {
                            questionFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        for (i = 4; i < args.length; i++) {
                            noQuestions++;
                            String id = args[i].replace("-question-" + noQuestions + " ", "");
                            id = id.replace("'", "");
                            found = false;
                            try (BufferedReader br = new BufferedReader(new FileReader("Questions.csv"))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    if (line.split(",")[0].equals(id)) {
                                        found = true;
                                        questions[noQuestions - 1] = new Question(line);
                                    }
                                }
                            } catch (IOException e) {
                                System.out.println(e);
                            }
                            if (!found) {
                                System.out.println(finalMessageError("Question ID for question " + id + " does not exist"));
                                return;
                            }
                        }
                        // cream quiz-ul si il salvam
                        String tempUser = args[1].replace("-u ", "");
                        tempUser = tempUser.replace("'", "");
                        try (FileWriter fw = new FileWriter("Quizzes.csv", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
                            quiz = new Quiz(name, tempUser, noQuestions, questions);
                            out.println(quiz.toString());
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        System.out.println(finalMessageOk("Quizz added succesfully"));
                    }
                    break;
                ///////////////////////////////////////////
                /*      6. ID chestionar dupa nume       */
                //////////////////////////////////////////
                case "-get-quizz-by-name":
                    if (args.length < 3) {
                        System.out.println(finalMessageError("You need to be authenticated"));
                    } else if (login(args[1], args[2])) {
                        String givenName;
                        givenName = args[3].replace("-text ", "");
                        givenName = givenName.replace("'", "");
                        File quizFile = new File("Quizzes.csv");
                        if (!quizFile.exists()) try {
                            quizFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        try (BufferedReader br = new BufferedReader(new FileReader("Quizzes.csv"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.contains(givenName)) {
                                    System.out.println(finalMessageOk(line.split(",")[0]));
                                    return;
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        // not found
                        System.out.println(finalMessageError("Quizz does not exist"));
                        return;
                    }
                    break;
                ////////////////////////////////////////
                /*      7. Afisare chestionare       */
                ///////////////////////////////////////
                case "-get-all-quizzes":
                    if (args.length < 3) {
                        System.out.println(finalMessageError("You need to be authenticated"));
                    } else if (login(args[1], args[2])) {
                        String temp = "[";
                        File quizFile = new File("Quizzes.csv");
                        if (!quizFile.exists()) try {
                            quizFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        try (BufferedReader br = new BufferedReader(new FileReader("Quizzes.csv"))) {
                            String line;
                            found = false;
                            while ((line = br.readLine()) != null) {
                                if (found) temp += ", ";
                                else found = true;
                                temp += "{\"quizz_id\" : \"" + line.split(",")[0] + "\", ";
                                temp += "\"quizz_name\" : \"" + line.split(",")[1] + "\", ";
                                temp += "\"is_completed\" : \"" + line.split(",")[3] + "\"}";
                            }
                            temp += "]";
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        System.out.println(finalMessageOk(temp));
                    }
                    break;
                /////////////////////////////////////////////
                /*      8. Detalii chestionar dupa ID       */
                ////////////////////////////////////////////
                case "-get-quizz-details-by-id":
                    if (args.length < 3) {
                        System.out.println(finalMessageError("You need to be authenticated"));
                    } else if (login(args[1], args[2])) {
                        String id = args[3].replace("-id ", "");
                        id = id.replace("'", "");

                        // cautam linia quiz-ului dat si o salvam
                        String[] quizLine = new String[5 + 10];
                        File quizFile = new File("Quizzes.csv");
                        if (!quizFile.exists()) try {
                            quizFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        try (BufferedReader br = new BufferedReader(new FileReader("Quizzes.csv"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.indexOf(id) == 0) {
                                    quizLine = line.split(",");
                                    break;
                                } else System.out.println(line);
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        // pe quizLine se afla toate informatiile quiz-ului cautat
                        int noQuestions = Integer.parseInt(quizLine[4]);
                        File questionFile = new File("Questions.csv");
                        Question[] questions = new Question[noQuestions];
                        if (!questionFile.exists()) try {
                            quizFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        for (i = 1; i <= noQuestions; i++) {
                            try (BufferedReader br = new BufferedReader(new FileReader("Questions.csv"))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    if (line.indexOf(quizLine[4 + i]) == 0) {
                                        questions[i - 1] = new Question(line);
                                        break;
                                    }
                                }
                            } catch (IOException e) {
                                System.out.println(e);
                            }
                        }
                        // detaliile quiz-ului
                        quiz = new Quiz();
                        quiz.setNoQuestions(noQuestions);
                        quiz.setQuestions(questions);
                        System.out.println(finalMessageOk(quiz.details()));
                    }
                    break;
                /////////////////////////////////////////////////
                /*      9. Incarca raspunsuri chestionar       */
                ////////////////////////////////////////////////
                case "-submit-quizz":
                    if (args.length < 3) {
                        System.out.println(finalMessageError("You need to be authenticated"));
                    } else if (login(args[1], args[2])) {
                        // verificam daca exista quiz-ul dat si il salvam
                        String givenID;
                        if (args.length == 3) {
                            System.out.println(finalMessageError("No quizz identifier was provided"));
                            return;
                        }

                        String quizID = args[3].replace("-quiz-id ", "");
                        quizID = quizID.replace("'", "");
                        File quizFile = new File("Quizzes.csv");
                        if (!quizFile.exists()) try {
                            quizFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        found = false;
                        String quizLine = new String();
                        try (BufferedReader br = new BufferedReader(new FileReader("Quizzes.csv"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.indexOf(quizID) == 0) {
                                    found = true;
                                    quizLine = line;
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        if (!found) {
                            System.out.println(finalMessageError("No quiz was found"));
                            return;
                        }
                        quiz = new Quiz(quizLine);
                        // verificam daca acest quiz a fost deja completat
                        if (quiz.completed) {
                            System.out.println(finalMessageError("You already submitted this quizz"));
                            return;
                        }
                        // verificam daca acest quiz a fost creat de acelasi user
                        user = args[1].replace("-u ", "");
                        user = user.replace("'", "");
                        if (user.equals(quiz.user)) {
                            System.out.println(finalMessageError("You cannot answer your own quizz"));
                            return;
                        }
                        // salvam intrebarile quiz-ului
                        File questionFile = new File("Questions.csv");
                        if (!questionFile.exists()) try {
                            questionFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        for (i = 0; i < quiz.noQuestions; i++) {
                            String questionLine = new String();
                            try (BufferedReader br = new BufferedReader(new FileReader("Questions.csv"))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    if (line.indexOf(String.valueOf(quiz.questions[i].id)) == 0) {
                                        questionLine = line;
                                        break;
                                    }
                                }
                            } catch (IOException e) {
                                System.out.println(e);
                            }
                            quiz.questions[i] = new Question(questionLine);
                        }
                        // verificam daca raspunsurile date exista in quiz
                        quiz.setPoints();
                        for (i = 4; i < args.length; i++) {
                            String temp = args[i].replace("-answer-id-" + (i - 3) + " ", "");
                            temp = temp.replace("'", "");
                            found = false;
                            for (int k = 0; k < quiz.noQuestions; k++) {
                                for (int j = 0; j < quiz.questions[k].noAnswers; j++) {
                                    if (quiz.questions[k].answers[j].id == Integer.parseInt(temp)) {
                                        found = true;
                                        if (quiz.questions[k].answers[j].correct)
                                            quiz.points += quiz.pozitivePoints(quiz.questions[k]);
                                        else quiz.points -= quiz.negativePoints(quiz.questions[k]);
                                        break;
                                    }
                                }
                                if (found) break;
                            }
                            if (!found) {
                                System.out.println(finalMessageError("Answer ID for answer " + (i - 3) + " does not exist"));
                                return;
                            }
                        }
                        // salvam punctaj
                        if (quiz.points < 0)
                            quiz.setPoints();
                        System.out.println(finalMessageOk(Math.round(quiz.points) + " points"));
                        // salvam solutia data
                        Solution solution = new Solution(user, quiz);
                        File solutionFile = new File("Solutions.csv");
                        if (!solutionFile.exists()) try {
                            solutionFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        try (FileWriter fw = new FileWriter("Solutions.csv", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
                            out.println(solution.toString());
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    }
                    break;
                /////////////////////////////////////////////
                /*      10. Stergere chestionar dupa ID       */
                ////////////////////////////////////////////
                case "-delete-quizz-by-id":
                    if (args.length < 3) {
                        System.out.println(finalMessageError("You need to be authenticated"));
                    } else if (login(args[1], args[2])) {
                        // verificam daca este dat id-ul quiz-ului
                        if (args.length == 3) {
                            System.out.println(finalMessageError("No quizz identifier was provided"));
                            return;
                        }
                        // verificam daca am primit un id corect
                        // cream o lista de quiz-uri in care salvam toate datele din Quizzes.csv, fara cel care trebuie eliminat
                        File quizFile = new File("Quizzes.csv");
                        if (!quizFile.exists()) try {
                            quizFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        String quizID = args[3].replace("-id ", "");
                        quizID = quizID.replace("'", "");
                        List<Quiz> quizzes = new ArrayList<>();
                        Quiz tempQuiz;
                        Quiz foundQuiz = new Quiz();
                        found = false;
                        try (BufferedReader br = new BufferedReader(new FileReader("Quizzes.csv"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (!found && line.indexOf(quizID) == 0) {
                                    found = true;
                                    foundQuiz = new Quiz(line);
                                } else {
                                    tempQuiz = new Quiz(line);
                                    quizzes.add(tempQuiz);
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        if (!found) {
                            System.out.println(finalMessageError("No quiz was found"));
                            return;
                        }
                        // verificam daca user-ul curent a creat chestionarul
                        user = args[1].replace("-u ", "");
                        user = user.replace("'", "");
                        if (!user.equals(foundQuiz.user)) {
                            System.out.println(finalMessageError("The user did not create this quiz"));
                            return;
                        }
                        // rescriem Quizzes.csv, folosind lista creata
                        quizFile.delete();
                        try {
                            quizFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        for (i = 0; i < quizzes.size(); i++) {
                            try (FileWriter fw = new FileWriter("Quizzes.csv", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
                                out.println(quizzes.get(i).toString());
                            } catch (IOException e) {
                                System.out.println(e);
                            }
                        }
                        // cream o lista de solutii in care salvam toate datele din Solutions.csv, fara cele ale quiz-ului eliminat
                        File solutionFile = new File("Solutions.csv");
                        if (!solutionFile.exists()) try {
                            solutionFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        List<Solution> solutions = new ArrayList<>();
                        Solution tempSolution;
                        try (BufferedReader br = new BufferedReader(new FileReader("Solutions.csv"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (!quizID.equals(line.split(",")[1])) {
                                    tempSolution = new Solution(line);
                                    solutions.add(tempSolution);
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        // rescriem Solutions.csv, folosind lista creata
                        solutionFile.delete();
                        try {
                            solutionFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        try (FileWriter fw = new FileWriter("Solutions.csv", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
                            for (i = 0; i < solutions.size(); i++) {
                                out.println(solutions.get(i).toString());
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        // totul a mers bine
                        System.out.println(finalMessageOk("Quizz deleted successfully"));
                        return;
                    }
                    break;
                ////////////////////////////////////////
                /*      11. Intoarce solutiile       */
                ///////////////////////////////////////
                case "-get-my-solutions":
                    if (args.length < 3) {
                        System.out.println(finalMessageError("You need to be authenticated"));
                    } else if (login(args[1], args[2])) {
                        String tempUser = args[1].replace("-u ", "");
                        tempUser = tempUser.replace("'", "");
                        String temp = new String();
                        int index = 0;
                        found = false;
                        File solutionFile = new File("Solutions.csv");
                        if (!solutionFile.exists()) try {
                            solutionFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        Solution tempSolution;
                        try (BufferedReader br = new BufferedReader(new FileReader("Solutions.csv"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (tempUser.equals(line.split(",")[0])) {
                                    if (!found) {
                                        found = true;
                                    } else temp += ", ";
                                    tempSolution = new Solution(line);
                                    index++;
                                    temp += "[{\"quiz-id\" : \"" + tempSolution.quiz.id + "\", \"quiz-name\" : \"" + tempSolution.quiz.name +
                                            "\", \"score\" : \"" + Math.round(tempSolution.quiz.points) + "\", \"index_in_list\" : \"" + index + "\"}]";
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        System.out.println(finalMessageOk(temp));
                    }
                    break;
                //////////////////////////////////////
                /*     12. Curatarea datelor        */
                /////////////////////////////////////
                case "-cleanup-all":
                    try {
                        File usersFile = new File("Users.csv");
                        File questionsFile = new File("Questions.csv");
                        File quizFile = new File("Quizzes.csv");
                        File solutionsFile = new File("Solutions.csv");
                        usersFile.delete();
                        questionsFile.delete();
                        quizFile.delete();
                        solutionsFile.delete();
                        Answer answer = new Answer();
                        answer.resetID();
                        question.resetID();
                        quiz.resetID();
                        System.out.println(finalMessageOk("Cleanup finished successfully"));
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
            }
        }
    }
}
