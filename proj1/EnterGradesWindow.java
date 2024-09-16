import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class EnterGradesWindow extends JFrame {
    private JTextField studentIdField;
    private JTextField subjectIdField;
    private JTextField subField;
    private JTextField marksField;

    public EnterGradesWindow() {
        setTitle("Enter Grades");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdField = new JTextField(20);
        JLabel subjectIdLabel = new JLabel("Subject ID:");
        subjectIdField = new JTextField(20);
        JLabel subLabel = new JLabel("Subject:");
        subField = new JTextField(20);
        JLabel marksLabel = new JLabel("Marks:");
        marksField = new JTextField(20);

        JButton submitButton = new JButton("Submit");

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(studentIdLabel, gbc);

        gbc.gridx = 1;
        contentPanel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(subjectIdLabel, gbc);

        gbc.gridx = 1;
        contentPanel.add(subjectIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(subLabel, gbc);

        gbc.gridx = 1;
        contentPanel.add(subField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(marksLabel, gbc);

        gbc.gridx = 1;
        contentPanel.add(marksField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        contentPanel.add(submitButton, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdField.getText();
                String subjectId = subjectIdField.getText();
                String subname = subField.getText();
                String marks = marksField.getText();

                if (studentExists(studentId)) {
                    if (gradesRecordExists(studentId, subjectId)) {
                        int choice = JOptionPane.showConfirmDialog(null, "Grades record already exists for Student ID: " + studentId + " and Subject ID: " + subjectId + ". Do you want to modify the marks?", "Grades Record Exists", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            updateGrades(studentId, subjectId, marks);
                            JOptionPane.showMessageDialog(null, "Grades updated for Student ID: " + studentId);
                        }
                    } else {
                        insertGrades(studentId, subjectId, subname, marks);
                        JOptionPane.showMessageDialog(null, "Grades submitted for Student ID: " + studentId);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Student ID not found. Please enter a valid Student ID.");
                }
            }
        });

        setContentPane(contentPanel);
    }

    private boolean studentExists(String studentId) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ptcs1",
                    "root", "root");

            String query = "SELECT stud_ID FROM slogin WHERE stud_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentId);

            ResultSet resultSet = preparedStatement.executeQuery();

            boolean exists = resultSet.next();

            preparedStatement.close();
            connection.close();

            return exists;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    private boolean gradesRecordExists(String studentId, String subjectId) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ptcs1",
                    "root", "root");

            String query = "SELECT stud_ID FROM grades WHERE stud_ID = ? AND subjectID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, subjectId);

            ResultSet resultSet = preparedStatement.executeQuery();

            boolean exists = resultSet.next();

            preparedStatement.close();
            connection.close();

            return exists;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    private void insertGrades(String studentId, String subjectId, String subname, String marks) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ptcs1",
                    "root", "root");

            String query = "INSERT INTO grades (stud_ID, subjectID, subname, marks) VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, subjectId);
            preparedStatement.setString(3, subname);
            preparedStatement.setString(4, marks);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private void updateGrades(String studentId, String subjectId, String marks) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ptcs1",
                    "root", "root");

            String query = "UPDATE grades SET marks = ? WHERE stud_ID = ? AND subjectID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, marks);
            preparedStatement.setString(2, studentId);
            preparedStatement.setString(3, subjectId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EnterGradesWindow enterGradesWindow = new EnterGradesWindow();
            enterGradesWindow.setVisible(true);
        });
    }
}
