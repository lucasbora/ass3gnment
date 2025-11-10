package controller;

import exceptions.DictionaryException;
import exceptions.ExpressionException;
import repository.IRepository;
import utils.IStack;
import exceptions.MyException;
import exceptions.StackException;
import state.PrgState;
import model.statement.IStmt;

public class Controller {
    private IRepository repo;
    private boolean displayFlag;
    public Controller(IRepository repo) {
        this.repo = repo;
        this.displayFlag = true;
    }
    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }
    public PrgState oneStep(PrgState prg) throws MyException, ExpressionException, DictionaryException {
        IStack<IStmt> stk = prg.getExeStack();
        if (stk.isEmpty()) {
            throw new MyException("Empty execution stack");
        }

        IStmt currentStmt;
        try {
            currentStmt = stk.pop();
        } catch (StackException e) {
            throw new MyException(e.getMessage());
        }
        return currentStmt.execute(prg);
    }

    public void allSteps() throws MyException, ExpressionException, DictionaryException {
        PrgState currentPrg = repo.getCurrentPrg();
        repo.logPrgStateExec(); // log initial state

        while (!currentPrg.getExeStack().isEmpty()) {
            oneStep(currentPrg);
            repo.logPrgStateExec(); // log after each step
        }
    }

    public void setDisplayFlag(boolean displayFlag) {
        this.displayFlag = displayFlag;
    }
}