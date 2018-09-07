package org.hjf.apt;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import org.hjf.annotation.apt.ApiRepository;
import org.hjf.annotation.apt.InstanceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "org.hjf.annotation.apt.ApiRepository",
})
public class ApiRepositoryProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        final String CLASS_NAME = "ApiRepository";

        // 1. 添加 ApiRepository.java 和其属性申明
        TypeSpec.Builder tb = TypeSpec.classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc("@Api仓库 此类由 {@link $L} 自动生成\n", ApiRepositoryProcessor.class.getName())
                .addJavadoc("所有标记 {@link $T} 都会生成单例对象保存在此处\n", ApiRepository.class);

        // 2. 循环获取标记注解  @ApiRepository 的接口
        //    生成对应接口的实现类，并在ApiRepository中申明各实现类的对象
        //    标记注解 ApiRepository.class 的所有接口的实现类的 TypeSpec.Builder
        List<TypeSpec.Builder> implClassList = new ArrayList<>();
        List<ClassName> classPathCache = new ArrayList<>();
        for (TypeElement element : ElementFilter.typesIn(roundEnvironment.getElementsAnnotatedWith(ApiRepository.class))) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "正在处理" + element.toString());
            ClassName className = ClassName.get(element);
            // 重复判断
            if (classPathCache.contains(className)) {
                continue;
            }
            classPathCache.add(className);

            // 4.1 获取实现类的代码
            implClassList.add(getImplementClassTypeSpecBuilder(element));

            // 4.1 申明变量字段： 实现类的对象
            ClassName implClassName = getInterfaceImplementClassName(className);
            tb.addField(FieldSpec.builder(implClassName, implClassName.simpleName())
                    .addJavadoc("@此字段 此类由 {@link $L} 自动生成\n", ApiRepositoryProcessor.class.getName())
                    .addJavadoc("是接口 {@link $L} 的实现类 {@link $L}的实例对象\n", className, implClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build());
        }

        // 5. 初始化方法： init(Retrofit retrofit)
        // 初始化所有 @ApiRepository 接口的对象
        tb.addMethod(MethodSpec.methodBuilder("init")
                .addJavadoc("@方法 此类由 {@link $L} 自动生成\n", ApiRepositoryProcessor.class.getName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ClassName.get("retrofit2", "Retrofit"), "retrofit")
                .addCode(getBlock4init(classPathCache))
                .build());

        // 6. 生成Java文件
        try {
            // 生成主类: ApiRepository.java
            JavaFile javaFile = JavaFile.builder(RouterProcessor.PACKAGE_NAME, tb.build()).build();
            javaFile.writeTo(mFiler);
            // 生成所有实现类
            for (TypeSpec.Builder builder : implClassList) {
                javaFile = JavaFile.builder(RouterProcessor.PACKAGE_NAME, builder.build()).build();
                javaFile.writeTo(mFiler);
            }
        } catch (IOException ignored) {
        }
        return true;
    }

    //  生成接口实现类
    private static TypeSpec.Builder getImplementClassTypeSpecBuilder(TypeElement typeElement) {
        ClassName className = ClassName.get(typeElement);
        ClassName implClassName = getInterfaceImplementClassName(className);
        // 1. 实现类声明
        TypeSpec.Builder tb = TypeSpec.classBuilder(implClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc("@Api仓库 此类由 {@link $L} 自动生成\n", ApiRepositoryProcessor.class.getName())
                .addJavadoc("此类是接口 {@link $T} 接口的实现类\n", className);

        // 2. 申明变量字段： 实现类的对象
        tb.addField(FieldSpec.builder(className, "service")
                .addJavadoc("@此字段 此类由 {@link $L} 自动生成\n", ApiRepositoryProcessor.class.getName())
                .addModifiers(Modifier.PRIVATE)
                .build());

        // 3. 实现类的构造方法
        tb.addMethod(MethodSpec.constructorBuilder()
                .addJavadoc("@构造方法 此类由 {@link $L} 自动生成\n", ApiRepositoryProcessor.class.getName())
                .addParameter(className, "service")
                .addCode(CodeBlock.builder().addStatement("this.service = service").build())
                .build());

        // 4. 实现接口标记的方法
        /**
         * TODO 先弄懂 retrofit 的参数注解
         *  FIXME 修改实现方式
         *  -   1. @Path - 从 params 中找
         *  -   2. @
         */
        for (Element element : typeElement.getEnclosedElements()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(element.getSimpleName().toString())
                    .addJavadoc("@此方法 此类由 {@link $L} 自动生成\n", ApiRepositoryProcessor.class.getName())
                    .returns(ClassName.get("okhttp3", "Call"))
                    .addModifiers(PUBLIC, STATIC);

            // 参数处理
            ExecutableElement executableElement = (ExecutableElement) element;
            for (int i = 0; i < executableElement.getParameters().size(); i++) {
                VariableElement vep = executableElement.getParameters().get(i);
                // TODO 如果含有普通参数注解，自动从 HashMap 中取
//                methodBuilder.addParameter(ParameterizedTypeName
//                        .get(HashMap.class, String.class, String.class), "params");
                // 普通参数：String，int
                switch (vep.getSimpleName().toString()) {
                    case "include":
                        break;
                    case "where":
                        break;
                    case "skip":
                        break;
                    case "limit":
                        break;
                    case "order":
                        break;
                }

            }
        }

        return tb;
    }

    private static CodeBlock getBlock4init(List<ClassName> classPathCache) {
        CodeBlock.Builder blockBuilder = CodeBlock.builder();
        for (ClassName className : classPathCache) {
            ClassName implClassName = getInterfaceImplementClassName(className);
            blockBuilder.addStatement("$T = new $T(retrofit.create($T.class))", implClassName, implClassName, className);
        }
        return blockBuilder.build();
    }

    private static ClassName getInterfaceImplementClassName(ClassName className) {
        return ClassName.get(RouterProcessor.PACKAGE_NAME, className.simpleName() + "Impl");
    }

    private CodeBlock getBlock(RoundEnvironment roundEnvironment) {
        CodeBlock.Builder blockBuilder = CodeBlock.builder();

        // switch
        blockBuilder.addStatement("String classPath = clazz.getName()");
        blockBuilder.beginControlFlow("switch (classPath)");

        // case ...
        ArrayList<ClassName> classNameCache = new ArrayList<>();
        for (TypeElement element : ElementFilter.typesIn(roundEnvironment.getElementsAnnotatedWith(InstanceFactory.class))) {
            ClassName className = ClassName.get(element);
            if (classNameCache.contains(className)) {
                continue;
            }
            classNameCache.add(className);

            blockBuilder.addStatement("case $S: return (T) new $T() ", className.toString(), className);
        }

        // default
        blockBuilder.addStatement("default: return (T) clazz.newInstance()");
        blockBuilder.endControlFlow();
        return blockBuilder.build();
    }
}
