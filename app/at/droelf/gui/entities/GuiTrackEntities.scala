package at.droelf.gui.entities

import java.util.UUID
import at.droelf.backend.DateTimeUtil
import org.joda.time.{DateTimeZone, DateTime}
import models.{Track, TrackMetaData, TrackPoint}
import play.api.Logger


case class GuiTrack(trackId: UUID, name: Option[String], activity: String, metaData: GuiTrackMetaData, trackPoints: Seq[GuiTrackPoint])

  object GuiTrack {
    def apply(track: Track, metaData: TrackMetaData, trkPt: Seq[TrackPoint]): GuiTrack = {
      GuiTrack(
        track.trackId,
        track.name,
        track.activity,
        GuiTrackMetaData(metaData),
        trkPt.map(GuiTrackPoint(_))
      )
    }

  }

  case class GuiTrackMetaData(description: Option[String],
                              distance: Option[Float],
                              timerTime: Option[Float],
                              totalElapsedTime: Option[Float],
                              movingTime: Option[Float],
                              stoppedTime: Option[Float],
                              movingSpeed: Option[Float],
                              maxSpeed: Option[Float],
                              maxElevation: Option[Float],
                              minElevation: Option[Float],
                              ascent: Option[Float],
                              descent: Option[Float],
                              avgAscentRate: Option[Float],
                              maxAscentRate: Option[Float],
                              avgDescentRate: Option[Float],
                              maxDescentRate: Option[Float],
                              calories: Option[Float],
                              avgHeartRate: Option[Float]){

    def +(other: GuiTrackMetaData): GuiTrackMetaData = GuiTrackMetaData(
        for(d1 <- this.description; d2 <- other.description) yield (d1 + ", " + d2),
        addOptionFloat(this.distance, other.distance),
        addOptionFloat(this.timerTime, other.timerTime),
        addOptionFloat(this.totalElapsedTime, other.totalElapsedTime),
        addOptionFloat(this.movingTime, other.movingTime),
        addOptionFloat(this.stoppedTime, other.stoppedTime),
        averageOptionFloat(this.movingSpeed, other.movingSpeed),
        maxOptionFloat(this.maxSpeed, other.maxSpeed),
        maxOptionFloat(this.maxElevation, other.maxElevation),
        minOptionFloat(this.minElevation, other.minElevation),
        addOptionFloat(this.ascent, other.ascent),
        addOptionFloat(this.descent, other.descent),
        averageOptionFloat(this.avgAscentRate, other.avgAscentRate),
        maxOptionFloat(this.maxAscentRate, other.maxAscentRate),
        averageOptionFloat(this.avgDescentRate, other.avgDescentRate),
        maxOptionFloat(this.maxDescentRate, other.maxDescentRate),
        addOptionFloat(this.calories, other.calories),
        averageOptionFloat(this.avgHeartRate, other.avgHeartRate)
      )
    def averageOptionFloat(o1: Option[Float], o2: Option[Float]): Option[Float] = unpackValues(o1,o2, (d1,d2) => ((d1 + d2)/2) )
    def minOptionFloat(o1: Option[Float], o2: Option[Float]): Option[Float] = unpackValues(o1,o2, (d1,d2) => (Math.min(d1, d2)) )
    def maxOptionFloat(o1: Option[Float], o2: Option[Float]): Option[Float] = unpackValues(o1,o2, (d1,d2) => (Math.max(d1, d2)))
    def addOptionFloat(o1: Option[Float], o2: Option[Float]): Option[Float] = unpackValues(o1,o2, (d1,d2) =>  (d1 + d2))

    def unpackValues(o1: Option[Float], o2: Option[Float], calc: (Float,Float) => Float): Option[Float] = (o1, o2) match {
        case (Some(f1), Some(f2)) => Some(calc(f1,f2))
        case (Some(f1), None) => Some(f1)
        case (None,Some(f2)) => Some(f2)
        case _ => None
    }
  }

  object GuiTrackMetaData {
    def apply(metaData: TrackMetaData): GuiTrackMetaData = GuiTrackMetaData(metaData.description, metaData.distance, metaData.timerTime, metaData.totalElapsedTime, metaData.movingTime, metaData.stoppedTime, metaData.movingSpeed, metaData.maxSpeed, metaData.maxElevation, metaData.minElevation, metaData.ascent, metaData.descent, metaData.avgAscentRate, metaData.maxAscentRate, metaData.avgDescentRate, metaData.maxDescentRate, metaData.calories, metaData.avgHeartRate)

    val empty = GuiTrackMetaData(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None)
  }

  case class GuiTrackPoint(latitude: Float, longitude: Float, elevation: Float, dateTime: DateTime)

  object GuiTrackPoint extends DateTimeUtil{
    def apply(trkPt: TrackPoint): GuiTrackPoint = GuiTrackPoint(trkPt.latitude, trkPt.longitude, trkPt.elevation, utcWithTimeZoneToDateTime(trkPt.dateTime, trkPt.dateTimeZone))
  }

