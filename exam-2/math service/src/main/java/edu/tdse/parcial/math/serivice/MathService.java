package edu.tdse.parcial.math.serivice;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MathService {

    /**
     * Method to calculate Collatz Sequence
     *
     * @param value value to start the sequence
     * @return sequence list of the values of the sequence
     */
    public List<Integer> collatzSequence(int value){
        int newValue = value;
        List<Integer> sequence = new ArrayList<>();

        sequence.add(value);

        while (newValue != 1){
            if(newValue % 2 == 0){
                newValue = newValue/2;
                sequence.add(newValue);
            } else {
                newValue = (3*newValue + 1);
                sequence.add(newValue);
            }
        }
        return sequence;
    }

}