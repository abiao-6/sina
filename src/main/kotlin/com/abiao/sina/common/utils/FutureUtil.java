package com.abiao.sina.common.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FutureUtil {


    /**
     * 阻塞等待futures中所有任务完成
     *
     * @param futures
     * @param <T>
     */
    public static <T> void allOf(List<CompletableFuture<T>> futures) {

        for (CompletableFuture<T> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 返回首先响应成功的请求
     *
     * @param futures
     * @param <T>
     * @return
     */
//    public static <T> T anyOf(List<CompletableFuture<T>> futures) {
//
//        CompletableFuture<T>[] completableFutures = toArray(futures);
//        //存储结果
//        T res = null;
//
////
////        CompletableFuture<T>[] completableFutures = toArray(futures);
////
////        CompletableFuture<Object> future = CompletableFuture.anyOf(completableFutures);
//
//        CompletableFuture<Object> future = CompletableFuture.anyOf(completableFutures);
//        if (isDoneNormally(future)) {
//            try {
//                return (T) future.get();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            } catch (ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//
//        }
//
//
//        Map<CompletableFuture<T>, Boolean> statusMap = futures.stream().collect(Collectors.toMap(x -> x, x -> false));
//
//        /**
//         * 循环判断任务是否正常完成,有一个正常完成,则返回
//         */
//        while (true) {
//            for (CompletableFuture<T> tCompletableFuture : futures) {
//                if (tCompletableFuture.isDone()) {
////                    statusMap.put(tCompletableFuture, )
//                }
//                if (isDoneNormally(tCompletableFuture)) {
//                    try {
//                        return tCompletableFuture.get();
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    } catch (ExecutionException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        }
//
//
////        return null;
//    }

//    /**
//     * @param futures
//     * @param <T>
//     * @return
//     */
//    private static <T> CompletableFuture<T>[] toArray(List<CompletableFuture<T>> futures) {
//        CompletableFuture[] array = new CompletableFuture[futures.size()];
//
//        return (CompletableFuture<T>[]) futures.toArray(array);
//    }
//
//    /**
//     * 判断目标future是否正常返回(无异常,无取消)
//     *
//     * @param future
//     * @return
//     */
//    public static boolean isDoneNormally(CompletableFuture future) {
//        return future.isDone() && !future.isCompletedExceptionally() && !future.isCancelled();
//    }
//
//
//    /**
//     * 反射获取Completable类相关信息
//     */
//    public static final Class<CompletableFuture> cfClass = CompletableFuture.class;
//    public static Class CompletableFuture_AnyOf;
//    public static Class CompletableFuture$Completion;
//
//    static {
//        try {
//            CompletableFuture$Completion = Class.forName("java.util.concurrent.CompletableFuture$Completion");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    static Constructor<CompletableFuture> constructor;
//    static Constructor anyCconstructor;
//    public static Field result;
//
//    public static Field NEXT;
//
//    public static Method encodeRelay;
//
//    public static Method unipush;
//
//    public static Method cleanStack;
//    public static Method tryPushStack;
//
//    public static Method tryFire;
//
//    static {
//        try {
//            CompletableFuture_AnyOf = Class.forName("java.util.concurrent.CompletableFuture$AnyOf");
//            anyCconstructor = CompletableFuture_AnyOf.getDeclaredConstructor(CompletableFuture.class, CompletableFuture.class, CompletableFuture[].class);
//            constructor = cfClass.getDeclaredConstructor(Object.class);
//            constructor.setAccessible(true);
//            anyCconstructor.setAccessible(true);
//        } catch (ClassNotFoundException | NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            cleanStack = cfClass.getDeclaredMethod("cleanStack");
//            cleanStack.setAccessible(true);
//            unipush = cfClass.getDeclaredMethod("unipush", CompletableFuture$Completion);
//            unipush.setAccessible(true);
//            encodeRelay = cfClass.getDeclaredMethod("encodeRelay", Object.class);
//            encodeRelay.setAccessible(true);
//            result = cfClass.getDeclaredField("result");
//            NEXT = cfClass.getDeclaredField("NEXT");
//            result.setAccessible(true);
//            tryPushStack = cfClass.getDeclaredMethod("tryPushStack", CompletableFuture$Completion);
//            tryPushStack.setAccessible(true);
//            tryFire = CompletableFuture$Completion.getDeclaredMethod("tryFire", int.class);
//            tryFire.setAccessible(true);
//        } catch (NoSuchMethodException | NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 获取到率先正常完成的Future的返回值
//     *
//     * @param cfs
//     * @param <T>
//     * @return
//     */
//    public static <T> T getAnyNormallyOf(List<CompletableFuture<T>> cfs) {
//        try {
//            return anyNormallyOf(cfs).get();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    /**
//     * 获取到率先正常完成的Future
//     *
//     * @param cfs
//     * @param <T>
//     * @return
//     */
//    public static <T> CompletableFuture<T> anyNormallyOf(List<CompletableFuture<T>> cfs) {
//        try {
//            return anyOf(cfs);
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        } catch (CloneNotSupportedException e) {
//            throw new RuntimeException(e);
//        } catch (InstantiationException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 获取到率先正常完成的Future,未捕捉异常
//     *
//     * @param cfs
//     * @param <T>
//     * @return
//     * @throws NoSuchMethodException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     * @throws CloneNotSupportedException
//     * @throws InstantiationException
//     */
//    private static <T> CompletableFuture<T> anyOf(List<CompletableFuture<T>> cfs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, CloneNotSupportedException, InstantiationException {
//        int n;
//        Object r;
//        //Future集合不超过一个
//        if ((n = cfs.size()) <= 1) {
//            return (n == 0)
//                    ? new CompletableFuture<T>() : ((CompletableFuture<T>) cfClass.getDeclaredMethod("uniCopyStage", CompletableFuture.class).invoke(cfClass, cfs.get(0)));
//        }
//        for (CompletableFuture<?> cf : cfs)
//            if ((r = result.get(cf)) != null && !cf.isCompletedExceptionally() && !cf.isCancelled()) {
//                return constructor.newInstance(encodeRelay.invoke(CompletableFuture.class, r));
//            }
//
//        CompletableFuture[] cfArray = new CompletableFuture[cfs.size()];
//        cfs.toArray(cfArray);
////        cfs = ObjUtil.clone(cfs);
//        CompletableFuture<Object> d = new CompletableFuture<>();
//        for (CompletableFuture<?> cf : cfArray) {
//
//            unipush(cf, anyCconstructor.newInstance(d, cf, cfArray));
////            unipush.invoke(cf, anyCconstructor.newInstance(d, cf, cfArray));
//        }
//        // If d was completed while we were adding completions, we should
//        // clean the stack of any sources that may have had completions
//        // pushed on their stack after d was completed.
//        if (isDoneNormally(d))
//            for (int i = 0, len = cfArray.length; i < len; i++)
//                if (isDoneNormally(cfArray[i]))
//                    for (i++; i < len; i++)
//                        if (isDoneNormally(cfArray[i]))
//                            cleanStack.invoke(cfArray[i]);
//        return (CompletableFuture<T>) d;
//    }
//
//
//    /**
//     * 重写CompletableFuture内部方法
//     *
//     * @param cf
//     * @param c
//     * @throws InvocationTargetException
//     * @throws IllegalAccessException
//     */
//    private final static void unipush(CompletableFuture cf, Object c) throws InvocationTargetException, IllegalAccessException {
//        if (c != null) {
//            while (!(boolean) tryPushStack.invoke(cf, c)) {
//                if (isDoneNormally(cf)) {
//                    NEXT.set(c, null);
//                    break;
//                }
//            }
//            if (isDoneNormally(cf))
//                tryFire.invoke(c, 0);
//        }
//    }
//

}
