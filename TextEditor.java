import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Scanner;

public class TextEditor extends JFrame implements ActionListener {


    ImageIcon icon = new ImageIcon("images\\logo.png");
    JTextArea textArea;
    JLabel fontLabel;
    JLabel wordCounterLabel;
    JScrollPane scrollPane;
    JSpinner spinner;
    JButton fontColorButton;
    JButton fontBold;
    JButton fontItalic;
    JComboBox fontBox;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    TextEditor() {
        this.setSize(600, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setIconImage(icon.getImage());
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        //-------Components------

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateWord();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateWord();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateWord();
            }
        });

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(490, 500));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        fontLabel = new JLabel("Font: ");
        wordCounterLabel = new JLabel("Words: ");
        wordCounterLabel.setPreferredSize(new Dimension(400,30));
        wordCounterLabel.setBackground(Color.WHITE);
        wordCounterLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        wordCounterLabel.setOpaque(true);

        spinner = new JSpinner();
        spinner.setPreferredSize(new Dimension(50, 25));
        spinner.setValue(20);


        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(), textArea.getFont().getStyle(), (int) spinner.getValue()));
            }
        });

        //-----Buttons----\\


        fontBold = new JButton("B");
        fontBold.addActionListener(this);
        fontBold.setFocusable(false);
        fontBold.setPreferredSize(new Dimension(45, 25));
        fontBold.setFont(new Font(fontBold.getFont().getFontName(), Font.BOLD, 14));

        fontItalic = new JButton("I");
        fontItalic.addActionListener(this);
        fontItalic.setFocusable(false);
        fontItalic.setPreferredSize(new Dimension(45, 25));
        fontItalic.setFont(new Font(fontItalic.getFont().getFontName(), Font.ITALIC, 14));

        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);
        fontColorButton.setFocusable(false);
        fontColorButton.setPreferredSize(new Dimension(70, 25));

        //-----/Buttons----\\

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");
        //------/Components-----

        //------Menu bar--------

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);


        //------/Menu bar--------

        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(spinner);
        this.add(fontBold);
        this.add(fontItalic);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.add(wordCounterLabel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fontColorButton) {
            JColorChooser chooser = new JColorChooser();

            Color color = chooser.showDialog(null, "Chose a color", Color.BLACK);

            textArea.setForeground(color);
        }
        if (e.getSource() == fontBox) {
            textArea.setFont(new Font((String) fontBox.getSelectedItem(), textArea.getFont().getStyle(), textArea.getFont().getSize()));
        }
        if (e.getSource() == fontBold) {
            if ((textArea.getFont().getStyle() & Font.BOLD) == 0) {
                textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getStyle() | Font.BOLD));
            } else {
                textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getStyle() & ~Font.BOLD));
            }
        }

        if (e.getSource()==fontItalic){
            if ((textArea.getFont().getStyle() & Font.ITALIC) == 0) {
                textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getStyle() | Font.ITALIC));
            } else {
                textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getStyle() & ~Font.ITALIC));
            }
        }


            if (e.getSource() == openItem) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text", "txt");
                fileChooser.setFileFilter(filter);
                int response = fileChooser.showOpenDialog(null);

                if (response == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    Scanner fileIn = null;

                    try {
                        fileIn = new Scanner(file);
                        if (file.isFile()) {
                            while (fileIn.hasNextLine()) {
                                String line = fileIn.nextLine() + "\n";
                                textArea.append(line);
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } finally {
                        fileIn.close();
                    }


                }
            }

            if (e.getSource() == saveItem) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                int response = fileChooser.showSaveDialog(null);

                if (response == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    PrintWriter fileOut = null;

                    try {
                        fileOut = new PrintWriter(file);
                        fileOut.println(textArea.getText());
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } finally {
                        fileOut.close();
                    }
                }
            }

            if (e.getSource() == exitItem) {
                System.exit(0);
            }
        }
    private void updateWord(){
        String text = textArea.getText();
        int wordCounter = text.isEmpty()? 0 :text.split("\\s").length;
        wordCounterLabel.setText("Words: "+wordCounter);
    }
    }

