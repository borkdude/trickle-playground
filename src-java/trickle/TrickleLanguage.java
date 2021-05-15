package trickle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleLanguage.ContextPolicy;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.instrumentation.AllocationReporter;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.api.source.Source;
@TruffleLanguage.Registration(id = TrickleLanguage.ID, name = "trickle", defaultMimeType = TrickleLanguage.MIME_TYPE, characterMimeTypes = TrickleLanguage.MIME_TYPE, contextPolicy = ContextPolicy.SHARED)
@ProvidedTags({StandardTags.CallTag.class, StandardTags.StatementTag.class, StandardTags.RootTag.class, StandardTags.RootBodyTag.class, StandardTags.ExpressionTag.class, DebuggerTags.AlwaysHalt.class,
                StandardTags.ReadVariableTag.class, StandardTags.WriteVariableTag.class})
public final class TrickleLanguage extends TruffleLanguage<TrickleContext> {
    public static volatile int counter;

    public static final String ID = "trickle";
    public static final String MIME_TYPE = "application/x-trickle";
    //private static final Source BUILTIN_SOURCE = Source.newBuilder(TrickleLanguage.ID, "", "Trickle builtin").build();
    public static RootNode rootNode;

    private final Assumption singleContext = Truffle.getRuntime().createAssumption("Single SL context.");

    //private final Map<NodeFactory<? extends SLBuiltinNode>, RootCallTarget> builtinTargets = new ConcurrentHashMap<>();
    //private final Map<String, RootCallTarget> undefinedFunctions = new ConcurrentHashMap<>();

    //private final Shape rootShape;

    public TrickleLanguage() {
        counter++;
        //this.rootShape = Shape.newBuilder().layout(SLObject.class).build();
    }

    @Override
    protected TrickleContext createContext(Env env) {
        return new TrickleContext(this, env);
    }

    // public RootCallTarget getOrCreateUndefinedFunction(String name) {
    //     RootCallTarget target = undefinedFunctions.get(name);
    //     if (target == null) {
    //         target = Truffle.getRuntime().createCallTarget(new SLUndefinedFunctionRootNode(this, name));
    //         RootCallTarget other = undefinedFunctions.putIfAbsent(name, target);
    //         if (other != null) {
    //             target = other;
    //         }
    //     }
    //     return target;
    // }

    // public RootCallTarget lookupBuiltin(NodeFactory<? extends SLBuiltinNode> factory) {
    //     RootCallTarget target = builtinTargets.get(factory);
    //     if (target != null) {
    //         return target;
    //     }

    //     /*
    //      * The builtin node factory is a class that is automatically generated by the Truffle DSL.
    //      * The signature returned by the factory reflects the signature of the @Specialization
    //      *
    //      * methods in the builtin classes.
    //      */
    //     int argumentCount = factory.getExecutionSignature().size();
    //     SLExpressionNode[] argumentNodes = new SLExpressionNode[argumentCount];
    //     /*
    //      * Builtin functions are like normal functions, i.e., the arguments are passed in as an
    //      * Object[] array encapsulated in SLArguments. A SLReadArgumentNode extracts a parameter
    //      * from this array.
    //      */
    //     for (int i = 0; i < argumentCount; i++) {
    //         argumentNodes[i] = new SLReadArgumentNode(i);
    //     }
    //     /* Instantiate the builtin node. This node performs the actual functionality. */
    //     SLBuiltinNode builtinBodyNode = factory.createNode((Object) argumentNodes);
    //     builtinBodyNode.addRootTag();
    //     /* The name of the builtin function is specified via an annotation on the node class. */
    //     String name = lookupNodeInfo(builtinBodyNode.getClass()).shortName();
    //     builtinBodyNode.setUnavailableSourceSection();

    //     /* Wrap the builtin in a RootNode. Truffle requires all AST to start with a RootNode. */
    //     SLRootNode rootNode = new SLRootNode(this, new FrameDescriptor(), builtinBodyNode, BUILTIN_SOURCE.createUnavailableSection(), name);

    //     /*
    //      * Register the builtin function in the builtin registry. Call targets for builtins may be
    //      * reused across multiple contexts.
    //      */
    //     RootCallTarget newTarget = Truffle.getRuntime().createCallTarget(rootNode);
    //     RootCallTarget oldTarget = builtinTargets.put(factory, newTarget);
    //     if (oldTarget != null) {
    //         return oldTarget;
    //     }
    //     return newTarget;
    // }

    // public static NodeInfo lookupNodeInfo(Class<?> clazz) {
    //     if (clazz == null) {
    //         return null;
    //     }
    //     NodeInfo info = clazz.getAnnotation(NodeInfo.class);
    //     if (info != null) {
    //         return info;
    //     } else {
    //         return lookupNodeInfo(clazz.getSuperclass());
    //     }
    // }

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        Source source = request.getSource();
        //System.out.println(source);
        //Map<String, RootCallTarget> functions;
        /*
         * Parse the provided source. At this point, we do not have a SLContext yet. Registration of
         * the functions with the SLContext happens lazily in SLEvalRootNode.
         */
        // if (request.getArgumentNames().isEmpty()) {
        //     functions = SimpleLanguageParser.parseSL(this, source);
        // } else {
        //     StringBuilder sb = new StringBuilder();
        //     sb.append("function main(");
        //     String sep = "";
        //     for (String argumentName : request.getArgumentNames()) {
        //         sb.append(sep);
        //         sb.append(argumentName);
        //         sep = ",";
        //     }
        //     sb.append(") { return ");
        //     sb.append(source.getCharacters());
        //     sb.append(";}");
        //     String language = source.getLanguage() == null ? ID : source.getLanguage();
        //     Source decoratedSource = Source.newBuilder(language, sb.toString(), source.getName()).build();
        //     functions = SimpleLanguageParser.parseSL(this, decoratedSource);
        // }

        //RootCallTarget main = functions.get("main");
        //RootNode evalMain;
        // if (main != null) {
        //     /*
        //      * We have a main function, so "evaluating" the parsed source means invoking that main
        //      * function. However, we need to lazily register functions into the SLContext first, so
        //      * we cannot use the original SLRootNode for the main function. Instead, we create a new
        //      * SLEvalRootNode that does everything we need.
        //      */
        //     //evalMain = new SLEvalRootNode(this, main, functions);
        // } else {
        //     /*
        //      * Even without a main function, "evaluating" the parsed source needs to register the
        //      * functions into the SLContext.
        //      */
        //     //evalMain = new SLEvalRootNode(this, null, functions);
        // }
        //rootNode = new TrickleRootNode()
        return Truffle.getRuntime().createCallTarget(rootNode);
    }

    @Override
    protected void initializeMultipleContexts() {
        singleContext.invalidate();
    }

    public boolean isSingleContext() {
        return singleContext.isValid();
    }

    // @Override
    // protected Object getLanguageView(SLContext context, Object value) {
    //     return SLLanguageView.create(value);
    // }

    /*
     * Still necessary for the old SL TCK to pass. We should remove with the old TCK. New language
     * should not override this.
     */
    // @SuppressWarnings("deprecation")
    // @Override
    // protected Object findExportedSymbol(SLContext context, String globalName, boolean onlyExplicit) {
    //     return context.getFunctionRegistry().lookup(globalName, false);
    // }

    // @Override
    // protected boolean isVisible(SLContext context, Object value) {
    //     return !InteropLibrary.getFactory().getUncached(value).isNull(value);
    // }

    // @Override
    // protected Object getScope(SLContext context) {
    //     return context.getFunctionRegistry().getFunctionsObject();
    // }

    // public Shape getRootShape() {
    //     return rootShape;
    // }

    // /**
    //  * Allocate an empty object. All new objects initially have no properties. Properties are added
    //  * when they are first stored, i.e., the store triggers a shape change of the object.
    //  */
    // public SLObject createObject(AllocationReporter reporter) {
    //     reporter.onEnter(null, 0, AllocationReporter.SIZE_UNKNOWN);
    //     SLObject object = new SLObject(rootShape);
    //     reporter.onReturnValue(object, 0, AllocationReporter.SIZE_UNKNOWN);
    //     return object;
    // }

    public static TrickleContext getCurrentContext() {
        return getCurrentContext(TrickleLanguage.class);
    }

    // private static final List<NodeFactory<? extends SLBuiltinNode>> EXTERNAL_BUILTINS = Collections.synchronizedList(new ArrayList<>());

    // public static void installBuiltin(NodeFactory<? extends SLBuiltinNode> builtin) {
    //     EXTERNAL_BUILTINS.add(builtin);
    // }

}