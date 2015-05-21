package com.siberika.idea.pascal.lang.compiled;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiManager;
import com.siberika.idea.pascal.util.ModuleUtil;

/**
 * Author: George Bakhtadze
 * Date: 21/05/2015
 */
public class PPUFileImpl extends CompiledFileImpl {
    public PPUFileImpl(PsiManager myManager, FileViewProvider provider) {
        super(myManager, provider);
    }

    @Override
    public String decompile(PsiManager manager, VirtualFile file) {
        return PPUFileDecompiler.decompileText(file.getPath(), ModuleUtil.getModuleForFile(manager.getProject(), file));
    }
}
