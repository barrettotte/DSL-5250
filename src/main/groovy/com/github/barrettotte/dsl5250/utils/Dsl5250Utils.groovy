package com.github.barrettotte.dsl5250.utils

import java.awt.Dimension
import java.nio.file.Paths

import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JPasswordField
import javax.swing.JTextField

import com.github.barrettotte.dsl5250.exception.MaskedInputException

class Dsl5250Utils{

    static Closure closureFromFile(final String fileName){
        closureFromFile(new File(fileName))
    }

    static Closure closureFromFile(final URL url){
        closureFromFile(Paths.get(url.toURI()).toFile())
    }

    static Closure closureFromFile(final File f){
        return new GroovyShell().evaluate('return ' + f.text)
    }

    // get masked string, interactively (console or swing GUI)
    static String getMaskedInput(final String prompt) {
        if (System.console() != null){
            return System.console().readPassword(prompt)
        }
        final JPanel panel = new JPanel()
        final JPasswordField pwd = new JPasswordField(16)
        final String[] options = ['OK', 'Cancel']
        panel.add(new JLabel(prompt))
        panel.add(pwd)
        
        final int option = JOptionPane.showOptionDialog(null, panel, prompt, JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0])
        if(option == 0){
            return pwd.getPassword()
        }
        throw new MaskedInputException('Masked input was not entered.')
    }

}