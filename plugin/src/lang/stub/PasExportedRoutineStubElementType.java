package com.siberika.idea.pascal.lang.stub;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.psi.stubs.ILightStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.siberika.idea.pascal.PascalLanguage;
import com.siberika.idea.pascal.lang.psi.PascalExportedRoutine;
import com.siberika.idea.pascal.lang.psi.field.ParamModifier;
import com.siberika.idea.pascal.lang.psi.impl.PasExportedRoutineImpl;
import com.siberika.idea.pascal.lang.psi.impl.PasField;
import kotlin.reflect.jvm.internal.impl.utils.SmartList;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Author: George Bakhtadze
 * Date: 13/10/2015
 */
public class PasExportedRoutineStubElementType extends ILightStubElementType<PasExportedRoutineStub, PascalExportedRoutine> {

    public static PasExportedRoutineStubElementType INSTANCE;

    public PasExportedRoutineStubElementType(String debugName) {
        super(debugName, PascalLanguage.INSTANCE);
        INSTANCE = this;
    }

    @Override
    public PasExportedRoutineStub createStub(LighterAST tree, LighterASTNode node, StubElement parentStub) {
        return new PasExportedRoutineStubImpl(parentStub, "-", PasField.Visibility.PUBLIC,
                "", false, false, false, "--", null, Collections.emptyList(), null);
    }

    @Override
    public PascalExportedRoutine createPsi(@NotNull PasExportedRoutineStub stub) {
        return new PasExportedRoutineImpl(stub, this);
    }

    @NotNull
    @Override
    public PasExportedRoutineStub createStub(@NotNull PascalExportedRoutine psi, StubElement parentStub) {
        return new PasExportedRoutineStubImpl(parentStub, psi.getName(), psi.getVisibility(),
                psi.getContainingUnitName(), psi.isConstructor(), psi.isFunction(), psi.isLocal(),
                psi.getFunctionTypeStr(), psi.getFormalParameterNames(), psi.getFormalParameterTypes(), psi.getFormalParameterAccess());
    }

    @NotNull
    @Override
    public String getExternalId() {
        return "pas.stub.routine";
    }

    @Override
    public void serialize(@NotNull PasExportedRoutineStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        StubUtil.printStub("PasExpRoutineStub.serialize", stub);

        dataStream.writeName(stub.getName());
        dataStream.writeName(stub.getVisibility().name());
        dataStream.writeName(stub.getContainingUnitName());
        dataStream.writeBoolean(stub.isConstructor());
        dataStream.writeBoolean(stub.isFunction());
        dataStream.writeBoolean(stub.isLocal());
        dataStream.writeName(stub.getFunctionTypeStr());
        StubUtil.writeStringCollection(dataStream, stub.getFormalParameterNames());
        StubUtil.writeStringCollection(dataStream, stub.getFormalParameterTypes());
        StubUtil.writeEnumCollection(dataStream, stub.getFormalParameterAccess());
    }

    @NotNull
    @Override
    public PasExportedRoutineStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        String name = StubUtil.readName(dataStream);
        PasField.Visibility visibility = StubUtil.readEnum(dataStream, PasField.Visibility.class);
        String containingUnitName = StubUtil.readName(dataStream);
        boolean constructor = dataStream.readBoolean();
        boolean function = dataStream.readBoolean();
        boolean local = dataStream.readBoolean();
        String typeStr = StubUtil.readName(dataStream);
        List<String> parameterNames = new SmartList<>();
        StubUtil.readStringCollection(dataStream, parameterNames);
        List<String> parameterTypes = new SmartList<>();
        StubUtil.readStringCollection(dataStream, parameterTypes);
        List<ParamModifier> parameterAccess = new SmartList<>();
        StubUtil.readEnumCollection(dataStream, parameterAccess, ParamModifier.values());
        return new PasExportedRoutineStubImpl(parentStub, name, visibility, containingUnitName, constructor, function, local,
                typeStr, parameterNames, parameterTypes, parameterAccess);
    }

    @Override
    public void indexStub(@NotNull PasExportedRoutineStub stub, @NotNull IndexSink sink) {
        sink.occurrence(PascalSymbolIndex.KEY, stub.getName());
        sink.occurrence(PascalUnitSymbolIndex.KEY, stub.getName().toUpperCase());
    }
}
