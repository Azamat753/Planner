package com.lawlett.planner.ui.fragment.intro

import android.graphics.Color
import android.os.Build
import su.levenetc.android.textsurface.Text
import su.levenetc.android.textsurface.TextBuilder
import su.levenetc.android.textsurface.TextSurface
import su.levenetc.android.textsurface.animations.*
import su.levenetc.android.textsurface.contants.Align
import su.levenetc.android.textsurface.contants.Pivot
import su.levenetc.android.textsurface.contants.Side


class TextSurfaceHelper {

  companion object {
      fun play(textSurface: TextSurface) {
          val textHello: Text = TextBuilder
              .create("Вас приветствует")
              .setSize(30F)
              .setAlpha(0)
              .setColor(Color.WHITE)
              .setPosition(Align.SURFACE_CENTER).build()
          val textHey: Text = TextBuilder
              .create("мобильное приложения")
              .setSize(26F)
              .setAlpha(0)
              .setColor(Color.RED)
              .setPosition(Align.BOTTOM_OF, textHello).build()
          val text1: Text = TextBuilder
              .create("для тайм-менеджмента")
              .setSize(26F)
              .setAlpha(0)
              .setColor(Color.WHITE)
              .setPosition(Align.RIGHT_OF, textHey).build()
          val textHi: Text = TextBuilder
              .create("Planner")
              .setSize(74F)
              .setAlpha(0)
              .setColor(Color.RED)
              .setPosition(Align.BOTTOM_OF, text1).build()
          val textHelloWorld: Text = TextBuilder
              .create("+")
              .setSize(64F)
              .setAlpha(0)
              .setColor(Color.RED)
              .setPosition(Align.BOTTOM_OF, textHi).build()
          val textRandomText: Text = TextBuilder
              .create("Давайте познокомимся")
              .setSize(26F)
              .setAlpha(0)
              .setColor(Color.WHITE)
              .setPosition(Align.BOTTOM_OF, textHelloWorld).build()
          val textNewRandomText: Text = TextBuilder
              .create("Что я умею делать ?")
              .setSize(28F)
              .setAlpha(0)
              .setColor(Color.WHITE)
              .setPosition(Align.BOTTOM_OF, textRandomText).build()

//        textSurface.play(
//                Sequential(
//                        Slide.showFrom(Side.TOP, textHello, 500),
//                        Delay.duration(500),
//                        Alpha.hide(textHello, 1500)
//                )
//        );

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
              textSurface.play(
                  Sequential(
                      ShapeReveal.create(textHello, 750, SideCut.show(Side.LEFT), false),
                      Parallel(
                          ShapeReveal.create(textHello, 600, SideCut.hide(Side.LEFT), false),
                          Sequential(
                              Delay.duration(300), ShapeReveal.create(
                                  textHello, 600, SideCut.show(Side.LEFT),
                                  false
                              )
                          )
                      ),
                      Parallel(
                          TransSurface(500, textHey, Pivot.CENTER),
                          ShapeReveal.create(textHey, 1300, SideCut.show(Side.LEFT), false)
                      ),
                      Delay.duration(500),  //                            new AnimationsSet(TYPE.PARALLEL,
                      //                                    Slide.showFrom(Side.TOP, text1, 500),
                      //                                    new TransSurface(1000, text1, Pivot.CENTER)),
                      //                            Delay.duration(500),
                      Parallel(
                          TransSurface(
                              500,
                              text1,
                              Pivot.CENTER
                          ),  //                                Slide.showFrom(Side.LEFT, text1, 750)),
                          ShapeReveal.create(text1, 1300, SideCut.show(Side.LEFT), false)
                      ),
                      Delay.duration(500),
                      Parallel(
                          TransSurface.toCenter(textHi, 500),
                          Rotate3D.showFromSide(textHi, 750, Pivot.TOP)
                      ),
                      Delay.duration(500),
                      Parallel(
                          TransSurface(500, textHelloWorld, Pivot.CENTER),
                          ShapeReveal.create(textHelloWorld, 1300, SideCut.show(Side.LEFT), false)
                      ),
                      Delay.duration(500),
                      Parallel(
                          TransSurface(500, text1, Pivot.CENTER),
                          ShapeReveal.create(textRandomText, 1300, SideCut.show(Side.LEFT), false)
                      ),
                      Delay.duration(500),
                      Parallel(
                          TransSurface.toCenter(textNewRandomText, 500),
                          Rotate3D.showFromSide(textNewRandomText, 750, Pivot.RIGHT)
                      ),
                      Alpha.hide(textHello, 1500),
                      Alpha.hide(textHey, 1500),
                      Alpha.hide(text1, 1500),
                      Alpha.hide(textHi, 1500),
                      Alpha.hide(textHelloWorld, 1500),
                      Alpha.hide(textRandomText, 1500)
                  )
              )
          }
      }
  }
}