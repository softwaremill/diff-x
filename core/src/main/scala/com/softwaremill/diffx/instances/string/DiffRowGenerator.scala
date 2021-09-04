package com.softwaremill.diffx.instances.string

import java.util
import java.util.Collections
import scala.collection.JavaConverters._

private[instances] class DiffRowGenerator {

  /** Get the DiffRows describing the difference between original and revised texts using the given patch. Useful for
    * displaying side-by-side diff.
    *
    * @param original
    *   the original text
    * @param revised
    *   the revised text
    * @return
    *   the DiffRows between original and revised texts
    */
  def generateDiffRows[T](
      original: List[T],
      revised: List[T],
      equalizer: (T, T) => Boolean = (t1: T, t2: T) => t1 == t2
  ): List[DiffRow[T]] = {
    val patch = DiffUtils.diff(original, revised, equalizer)
    generateDiffRowsFromPatch(original.asJava, patch, revised.asJava).asScala.toList
  }

  private def generateDiffRowsFromPatch[T](
      original: util.List[T],
      patch: Patch[T],
      revised: util.List[T]
  ): util.List[DiffRow[T]] = {
    val diffRows = new util.ArrayList[DiffRow[T]]
    var endPos = 0
    val deltaList = patch.getDeltas
    for (originalDelta <- deltaList.asScala) {
      for (delta <- decompressDeltas(originalDelta).asScala) {
        endPos = transformDeltaIntoDiffRow(original, endPos, diffRows, delta, revised)
      }
    }
    // Copy the final matching chunk if any.
    for (line <- original.subList(endPos, original.size).asScala) {
      diffRows.add(buildDiffRow(DiffRow.Tag.EQUAL, Some(line), Some(line)))
    }
    diffRows
  }

  /** Transforms one patch delta into a DiffRow object.
    */
  private def transformDeltaIntoDiffRow[T](
      original: util.List[T],
      endPos: Int,
      diffRows: util.List[DiffRow[T]],
      delta: Delta[T],
      revised: util.List[T]
  ) = {
    val orig = delta.getSource
    val rev = delta.getTarget
    for (line <- original.subList(endPos, orig.position).asScala) {
      diffRows.add(buildDiffRow(DiffRow.Tag.EQUAL, Some(line), Some(revised.get(rev.position - 1))))
    }
    delta.getType match {
      case Delta.TYPE.INSERT =>
        for (line <- rev.lines) {
          diffRows.add(buildDiffRow(DiffRow.Tag.INSERT, None, Some(line)))
        }

      case Delta.TYPE.DELETE =>
        for (line <- orig.lines) {
          diffRows.add(buildDiffRow(DiffRow.Tag.DELETE, Some(line), None))
        }

      case _ =>
        for (j <- 0 until Math.max(orig.size, rev.size)) {
          diffRows.add(
            buildDiffRow(
              DiffRow.Tag.CHANGE,
              orig.lines.lift(j),
              rev.lines.lift(j)
            )
          )
        }
    }
    orig.last + 1
  }

  /** Decompresses ChangeDeltas with different source and target size to a ChangeDelta with same size and a following
    * InsertDelta or DeleteDelta. With this problems of building DiffRows getting smaller.
    *
    * @param deltaList
    */
  private def decompressDeltas[T](delta: Delta[T]): util.List[Delta[T]] = {
    if ((delta.getType == Delta.TYPE.CHANGE) && delta.getSource.size != delta.getTarget.size) {
      val deltas = new util.ArrayList[Delta[T]]
      val minSize = Math.min(delta.getSource.size, delta.getTarget.size)
      val orig = delta.getSource
      val rev = delta.getTarget
      deltas.add(
        new ChangeDelta[T](
          new Chunk[T](orig.position, orig.lines.slice(0, minSize)),
          new Chunk[T](rev.position, rev.lines.slice(0, minSize))
        )
      )
      if (orig.lines.size < rev.lines.size)
        deltas.add(
          new InsertDelta[T](
            new Chunk[T](orig.position + minSize, scala.List.empty),
            new Chunk[T](rev.position + minSize, rev.lines.slice(minSize, rev.lines.size))
          )
        )
      else
        deltas.add(
          new DeleteDelta[T](
            new Chunk[T](orig.position + minSize, orig.lines.slice(minSize, orig.lines.size)),
            new Chunk[T](rev.position + minSize, scala.List.empty)
          )
        )
      return deltas
    }
    Collections.singletonList(delta)
  }

  private def buildDiffRow[T](`type`: DiffRow.Tag, orgline: Option[T], newline: Option[T]) =
    new DiffRow(`type`, orgline, newline)
}
