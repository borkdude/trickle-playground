package trickle;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import clojure.lang.IFn;

// see https://github.com/graalvm/simplelanguage/blob/master/language/src/main/java/com/oracle/truffle/sl/nodes/expression/SLInvokeNode.java

public final class TrickleFnCallNode extends TrickleNode {
    @Child
    private TrickleNode fn;
    
    @Children
    private TrickleNode[] args;

    public TrickleFnCallNode(TrickleNode fn, TrickleNode[] args) {
        this.fn = fn;
        this.args = args;
    }

    @ExplodeLoop
    @Override
    public Object execute(VirtualFrame frame) {
        IFn fn = (IFn)this.fn.execute(frame);

        CompilerAsserts.compilationConstant(args.length);
        Object[] evaluated = new Object[args.length];

        for (int i = 0; i < args.length; i++) {
            evaluated[i]= args[i].execute(frame);
        }
        return fn.applyTo(clojure.lang.RT.seq(evaluated));
    }

    @Override
    public int executeInt(VirtualFrame frame) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
