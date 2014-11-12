#!/usr/bin/env python

from __future__ import print_function
from PyQt4.QtCore import *
from PyQt4.QtGui import *
import sys
import os


class WheelEncoderGeneratorApp(QMainWindow):

    ## TODO: put this into an OldFileFormat class
    # Encoder Types
    ABSOLUTE = 0
    STANDARD = 1
    # Code Types
    GRAY = 0
    BINARY = 1

    encoder_changed = pyqtSignal()
    encoder_saved = pyqtSignal()

    def __init__(self):
        super(WheelEncoderGeneratorApp, self).__init__()

        self.saved = True

        self.cwd = os.path.expanduser("~")+'/Documents'
        print(self.cwd)

        # Model
        self.std_encoder = StandardEncoder()
        self.abs_encoder = AbsoluteEncoder()
        self.type = self.STANDARD

        # Actions
        # Exit
        self.exit_action = QAction(QIcon('icons/Exit.png'), 'Exit', self)
        self.exit_action.setStatusTip('Exit application')
        self.exit_action.setShortcut('Ctrl+Q')
        self.exit_action.triggered.connect(self.do_exit)

        # New
        self.new_action = QAction(QIcon('icons/New_document.png'), 'New', self)
        self.new_action.setStatusTip('New encoder disc')
        self.new_action.triggered.connect(self.do_new)

        # Open
        self.open_action = QAction(QIcon('icons/Folder.png'), 'Open...', self)
        self.open_action.setStatusTip('Open encoder disc')
        self.open_action.setShortcut('Ctrl+O')
        self.open_action.triggered.connect(self.do_open)

        # Save
        self.save_action = QAction(QIcon('icons/Save.png'), 'Save', self)
        self.save_action.setStatusTip('Save encoder disc')
        self.save_action.setShortcut('Ctrl+S')
        self.save_action.triggered.connect(self.do_save)

        # Save As
        self.save_as_action = QAction(QIcon('icons/SaveAs.png'), 'Save As...', self)
        self.save_as_action.setStatusTip('Save encoder disc as new file')
        self.save_as_action.triggered.connect(self.do_save_as)

        # Export
        self.export_action = QAction(QIcon('icons/Picture.png'), 'Export...', self)
        self.export_action.setStatusTip('Export encoder disc as image')
        self.export_action.triggered.connect(self.do_export)

        # Print
        self.print_action = QAction(QIcon('icons/Print.png'), 'Print...', self)
        self.print_action.setStatusTip('Print encoder disc')
        self.print_action.triggered.connect(self.do_print)

        # Toolbars
        # Toolbar1
        self.toolbar1 = self.addToolBar('toolbar1')
        self.toolbar1.addAction(self.new_action)
        self.toolbar1.addAction(self.open_action)

        # Toolbar2
        self.toolbar2 = self.addToolBar('toolbar2')
        self.toolbar2.addAction(self.save_action)
        self.toolbar2.addAction(self.save_as_action)
        self.toolbar2.addAction(self.export_action)

        # Toolbar3
        self.toolbar3 = self.addToolBar('toolbar3')
        self.toolbar3.addAction(self.print_action)

        # Enable/disable icons
        self.connected = False
        self.running = False

        # Script file variables
        self.filename = ''

        # Controls

        # Standard Controls

        # Resolution
        self.std_res = QSpinBox()
        self.std_res.setStatusTip('Set resolution')
        self.std_res.setAlignment(Qt.AlignRight)
        self.std_res.setSingleStep(2)
        self.std_res.setMinimum(2)
        self.std_res.setMaximum(1024)
        std_res_label = QLabel(self.tr("&Resolution:"))
        std_res_label.setBuddy(self.std_res)

        # Quadrature track
        self.std_quadrature_track = QCheckBox('Quadrature track')
        self.std_quadrature_track.setStatusTip('Add a quadrature track')

        # Index track
        self.std_index_track = QCheckBox('Index track')
        self.std_index_track.setStatusTip('Add an index track')

        # Standard Layout
        std_layout = QGridLayout()
        std_layout.addWidget(std_res_label, 0, 0, 1, 1)
        std_layout.addWidget(self.std_res, 0, 1, 1, 1)
        std_layout.addWidget(self.std_quadrature_track, 1, 0, 1, 2)
        std_layout.addWidget(self.std_index_track, 2, 0, 1, 2)

        # Standard Widget
        self.std_tab = QWidget()
        self.std_tab.setLayout(std_layout)

        # Absolute Controls

        # Resolution
        self.abs_res = BinarySpinBox()
        self.abs_res.setAlignment(Qt.AlignRight)
        self.abs_res.setMinimum(2)
        self.abs_res.setMaximum(1024)
        self.abs_res.setStatusTip('Set resolution')
        abs_res_label = QLabel(self.tr("&Resolution:"))
        abs_res_label.setBuddy(self.abs_res)

        # Code
        self.abs_code_gray = QRadioButton('Gray code')
        self.abs_code_gray.setStatusTip('Use Gray code')
        self.abs_code_binary = QRadioButton('Binary code')
        self.abs_code_binary.setStatusTip('Use binary code')
        code = QButtonGroup()
        code.addButton(self.abs_code_gray)
        code.addButton(self.abs_code_binary)

        # Absolute Layout
        abs_layout = QGridLayout()
        abs_layout.addWidget(abs_res_label, 0, 0, 1, 1)
        abs_layout.addWidget(self.abs_res, 0, 1, 1, 1)
        abs_layout.addWidget(self.abs_code_gray, 1, 0, 1, 1)
        abs_layout.addWidget(self.abs_code_binary, 1, 1, 1, 1)

        # Absolute Widget
        self.abs_tab = QWidget()
        self.abs_tab.setLayout(abs_layout)

        # Controls Tabs
        self.controls_tabs = QTabWidget()
        # self.controls_tabs.addTab(self.std_tab, 'Standard')
        # self.controls_tabs.addTab(self.abs_tab, 'Absolute')
        self.std_index = 0
        self.abs_index = 1
        self.controls_tabs.insertTab(self.std_index, self.std_tab, 'Standard')
        self.controls_tabs.insertTab(self.abs_index, self.abs_tab, 'Absolute')

        # Dimensions Controls

        # Outer diameter text field
        self.dim_outer = QLineEdit()
        dim_outer_label = QLabel(self.tr('&Outer Diameter:'))
        dim_outer_label.setBuddy(self.dim_outer)

        # Inner diameter text field
        self.dim_inner = QLineEdit()
        dim_inner_label = QLabel(self.tr('&Inner Diameter:'))
        dim_inner_label.setBuddy(self.dim_inner)

        # Units
        self.units_mm = QRadioButton('mm')
        self.units_mm.setChecked(True)
        self.units_mm.setDisabled(True)
        self.units_in = QRadioButton('inch')
        self.units_in.setEnabled(False)
        units = QButtonGroup()
        units.addButton(self.units_mm)
        units.addButton(self.units_in)

        # Dimension layout
        dim_layout = QGridLayout()
        dim_layout.addWidget(dim_inner_label, 0, 0)
        dim_layout.addWidget(self.dim_inner, 0, 1)
        dim_layout.addWidget(dim_outer_label, 1, 0)
        dim_layout.addWidget(self.dim_outer, 1, 1)
        dim_layout.addWidget(self.units_mm, 2, 0)
        dim_layout.addWidget(self.units_in, 2, 1)

        # Controls Layout
        controls_layout = QVBoxLayout()
        controls_layout.addWidget(self.controls_tabs)
        controls_layout.addLayout(dim_layout)
        controls_layout.addStretch(1)

        controls_frame = QGroupBox('Controls')
        controls_frame.setLayout(controls_layout)

        # Disc Image
        self.drawing_frame = EncoderWidget()

        # Main layout
        main_layout = QHBoxLayout()
        main_layout.addWidget(self.drawing_frame, stretch=1)
        main_layout.addWidget(controls_frame)

        main_widget = QWidget()
        main_widget.setLayout(main_layout)
        self.setCentralWidget(main_widget)

        # Menu Bar
        menu_bar = self.menuBar()
        file_menu = menu_bar.addMenu('&File')
        file_menu.addAction(self.new_action)
        file_menu.addAction(self.open_action)
        file_menu.addSeparator()
        file_menu.addAction(self.save_action)
        file_menu.addAction(self.save_as_action)
        file_menu.addSeparator()
        file_menu.addAction(self.export_action)
        file_menu.addSeparator()
        file_menu.addAction(self.print_action)
        file_menu.addSeparator()
        file_menu.addAction(self.exit_action)

        self.default_height = 600
        self.default_width = 800

        # Signals/Slots
        # Standard
        self.std_res.valueChanged.connect(self.set_std_resolution)
        self.std_quadrature_track.stateChanged.connect(self.set_quadrature)
        self.std_index_track.stateChanged.connect(self.set_index)

        # Absolute
        self.abs_res.valueChanged.connect(self.set_abs_resolution)
        self.abs_code_binary.toggled.connect(self.set_code)
        self.controls_tabs.currentChanged.connect(self.set_encoder_type)

        # Both
        self.dim_inner.editingFinished.connect(self.set_inner)
        self.dim_outer.editingFinished.connect(self.set_outer)

        self.std_encoder.changed.connect(self.std_changed)
        self.abs_encoder.changed.connect(self.abs_changed)
        self.encoder_changed.connect(self.sync_changed)
        self.encoder_saved.connect(self.sync_saved)

        self.do_new()

        # Geometry
        self.setGeometry(50, 50, self.default_width, self.default_height)

        self.show()

    def abs_changed(self):
        print("Absolute changed")
        self.type = self.ABSOLUTE
        self.drawing_frame.update_image(self.abs_encoder)
        self.dim_inner.setText(str(self.abs_encoder.inner()))
        self.dim_outer.setText(str(self.abs_encoder.outer()))
        self.abs_res.setValue(self.abs_encoder.resolution())
        if self.abs_encoder.code() == AbsoluteEncoder.Binary:
            self.abs_code_binary.setChecked(True)
        elif self.abs_encoder.code() == AbsoluteEncoder.Gray:
            self.abs_code_gray.setChecked(True)
        self.controls_tabs.setCurrentIndex(self.abs_index)
        self.encoder_changed.emit()

    def std_changed(self):
        print("Standard changed")
        self.type = self.STANDARD
        self.drawing_frame.update_image(self.std_encoder)
        self.dim_inner.setText(str(self.std_encoder.inner()))
        self.dim_outer.setText(str(self.std_encoder.outer()))
        self.std_res.setValue(self.std_encoder.resolution())
        self.std_quadrature_track.setChecked(self.std_encoder.quadrature())
        self.std_index_track.setChecked(self.std_encoder.index())
        self.controls_tabs.setCurrentIndex(self.std_index)
        self.encoder_changed.emit()

## TODO Implement Inverse

    def display_filename(self):
        if self.filename:
            return self.filename
        else:
            return 'Untitled.weg'

    def sync_changed(self):
        self.saved = False
        self.save_action.setEnabled(True)
        self.setWindowTitle('WEG - *' + self.display_filename())

    def sync_saved(self):
        self.saved = True
        self.save_action.setDisabled(True)
        self.setWindowTitle('WEG - ' + self.display_filename())

    def set_encoder_type(self, evt):
        if evt == self.abs_index:
            self.abs_changed()

        elif evt == self.std_index:
            self.std_changed()

    def set_std_resolution(self):
        ## TODO: input validation, no odd numbers
        self.std_encoder.set_resolution(self.std_res.value())

    def set_abs_resolution(self):
        ## TODO: input validation: only 2^N values
        self.abs_encoder.set_resolution(self.abs_res.value())

    def set_inner(self):
        if self.type == self.ABSOLUTE:
            self.abs_encoder.set_inner(float(self.dim_inner.text()))
        elif self.type == self.STANDARD:
            self.std_encoder.set_inner(float(self.dim_inner.text()))

    def set_outer(self):
        if self.type == self.ABSOLUTE:
            self.abs_encoder.set_outer(float(self.dim_outer.text()))
        elif self.type == self.STANDARD:
            self.std_encoder.set_outer(float(self.dim_outer.text()))

    def set_quadrature(self):
        self.std_encoder.set_quadrature(self.std_quadrature_track.checkState() == Qt.Checked)

    def set_index(self):
        self.std_encoder.set_index(self.std_index_track.checkState() == Qt.Checked)

    def set_code(self):
        if self.abs_code_gray.isChecked():
            self.abs_encoder.set_code(AbsoluteEncoder.Gray)
        elif self.abs_code_binary.isChecked():
            self.abs_encoder.set_code(AbsoluteEncoder.Binary)

    def save_encoder(self, filename):
        pass

    def load_encoder(self, filename):
        # Old file format:
        #
        # #Wheel Encoder Settings
        # #Thu Jan 05 11:42:34 MST 2012
        # encoder.innerDiameter=15
        # encoder.quadratureTrack=false
        # encoder.outerDiameter=70
        # encoder.resolution=32
        # encoder.type=1
        # encoder.indexTrack=false
        # encoder.numbering=0
        # encoder.inverted=false
        #
        with open(filename, 'r') as f:
            data = f.readlines()
            f.close()

        ## TODO Old File Format vs. New?

        config = {}

        for line in data:
            if line.startswith('encoder.'):
                a = line.split('.')
                b = a[1].split('=')
                config[b[0]] = b[1].rstrip()

        try:
            if int(config['type']) == self.ABSOLUTE:
                self.type = self.ABSOLUTE
                self.abs_encoder.set_resolution(int(config['resolution']))
                self.abs_encoder.set_outer(float(config['outerDiameter']))
                self.abs_encoder.set_inner(float(config['innerDiameter']))
                if int(config['numbering']) == self.GRAY:
                    self.abs_encoder.set_code(AbsoluteEncoder.Gray)
                elif int(config['numbering']) == self.BINARY:
                    self.abs_encoder.set_code(AbsoluteEncoder.Binary)
                else:
                    print('unknown absolute encoder code %s' % config['numbering'])

            elif int(config['type']) == self.STANDARD:
                self.type = self.STANDARD
                self.std_encoder.set_resolution(int(config['resolution']))
                self.std_encoder.set_index(config['indexTrack'] == 'true')
                self.std_encoder.set_quadrature(config['quadratureTrack'] == 'true')
                self.std_encoder.set_outer(float(config['outerDiameter']))
                self.std_encoder.set_inner(float(config['innerDiameter']))

            else:
                QErrorMessage(self).showMessage('unknown encoder type %s' % config['type'])
        except KeyError as e:
            QErrorMessage(self).showMessage('WEG file is missing a key-value pair %s' % e)

        ## TODO fix inverted

    def check_modified(self):
        result = True
        if not self.saved:
            msg = QMessageBox(self)
            msg.setText('The encoder has been modified.')
            msg.setIcon(QMessageBox.Warning)
            msg.setInformativeText('Do you want to save your changes?')
            msg.setStandardButtons(QMessageBox.Save | QMessageBox.Discard | QMessageBox.Cancel)
            msg.setDefaultButton(QMessageBox.Save)
            msg.exec_()
            ret = msg.result()
            if ret == QMessageBox.Save:
                self.do_save()
                result = True
            elif ret == QMessageBox.Discard:
                result = True
            elif ret == QMessageBox.Cancel:
                result = False
            else:
                print('%s' % ret)
        return result

    def do_new(self):
        if self.check_modified():
            self.filename = ''

            # Defaults
            self.type = self.STANDARD

            self.abs_encoder.set_resolution(64)
            self.abs_encoder.set_outer(50.0)
            self.abs_encoder.set_inner(10.0)
            self.abs_encoder.set_code(AbsoluteEncoder.Binary)

            self.std_encoder.set_resolution(32)
            self.std_encoder.set_outer(50.0)
            self.std_encoder.set_inner(40.0)
            self.std_encoder.set_quadrature(False)
            self.std_encoder.set_index(False)

            self.encoder_saved.emit()

    def do_open(self):
        if self.check_modified():
            ## TODO remember last cwd; check changed
            os.chdir(self.cwd)
            filename = QFileDialog(self).getOpenFileName(parent=self,
                                                         caption=self.tr('Open Wheel Encoder Generator File'),
                                                         directory=self.cwd,
                                                         filter=self.tr("WEG Files (*.weg)"))
            if filename:
                try:
                    self.load_encoder(filename)
                except (IOError, OSError) as e:
                    QErrorMessage(self).showMessage('Error opening file %s ' % e)
                else:
                    self.filename = os.path.basename(str(filename))
                    self.encoder_saved.emit()

    def do_export(self):
        print('unsupported')

    def do_save(self, save_as=False):
        if save_as:
            filename = ''
        else:
            filename = self.filename

        if not filename:
            filename = QFileDialog(self).getSaveFileName(parent=self,
                                                         caption=self.tr('Save Wheel Encoder Generator File'),
                                                         directory=self.cwd,
                                                         filter=self.tr("WEG Files (*.weg)"))
        if filename:
            print("saving now")
            self.filename = os.path.basename(str(filename))
            self.encoder_saved.emit()

    def do_save_as(self):
        self.do_save(True)

    def do_print(self):
        printer = QPrinter()
        dialog = QPrintDialog(printer)
        dialog.setModal(True)
        dialog.setWindowTitle("Print Encoder")
        if dialog.exec_() == QDialog.Accepted:
            pixmap = QPixmap.grabWidget(self.drawing_frame)
            painter = QPainter(printer)
            painter.drawPixmap(0, 0, pixmap)
            del painter

    def do_exit(self):
        if self.check_modified():
            QApplication.quit()


## TODO: error checking with raise if incorrect inputs provided
class Encoder(QObject):
    changed = pyqtSignal()

    def __init__(self, resolution, outer, inner):
        super(QObject, self).__init__()
        self.res = resolution
        self.outer_dia = outer
        self.inner_dia = inner

    def set_resolution(self, resolution):
        self.res = resolution
        self.changed.emit()

    def set_outer(self, outer):
        self.outer_dia = float(outer)
        self.changed.emit()

    def set_inner(self, inner):
        self.inner_dia = float(inner)
        self.changed.emit()

    def resolution(self):
        return self.res

    def outer(self):
        return float(self.outer_dia)

    def inner(self):
        return float(self.inner_dia)


class StandardEncoder(Encoder):
    def __init__(self, resolution=16, outer=50, inner=10, quadrature=False, index=False):
        super(StandardEncoder, self).__init__(resolution, outer, inner)
        self.has_quad = quadrature
        self.has_index = index

    def set_quadrature(self, quadrature):
        if isinstance(quadrature, bool):
            self.has_quad = quadrature
            self.changed.emit()

    def set_index(self, index):
        if isinstance(index, bool):
            self.has_index = index
            self.changed.emit()

    def quadrature(self):
        return self.has_quad

    def index(self):
        return self.has_index


class AbsoluteEncoder(Encoder):
    Gray = 0
    Binary = 1

    def __init__(self, resolution=16, outer=50, inner=10, code=Gray):
        super(AbsoluteEncoder, self).__init__(resolution, outer, inner)
        if code == AbsoluteEncoder.Gray or code == AbsoluteEncoder.Binary:
            self.my_code = code

    def set_code(self, code):
        if code == AbsoluteEncoder.Gray or code == AbsoluteEncoder.Binary:
            self.my_code = code
            self.changed.emit()

    def code(self):
        return self.my_code


class EncoderWidget(QWidget):
    def __init__(self, parent=None):
        super(EncoderWidget, self).__init__(parent)
        # self.img = QImage(self)
        self.encoder = None
        self.paint = QPainter()

    # def image(self):
    #     return self.img

    def update_image(self, encoder):
        self.encoder = encoder
        self.repaint()

    ## TODO: Convert to drawing on a QImage and deal with "real" resolution/scaling

    def draw_track(self, paint, x, y, d, resolution=16, offset=False, color=Qt.black, index=False):
        assert isinstance(paint, QPainter)
        assert isinstance(d, float)
        assert isinstance(x, float)
        assert isinstance(y, float)
        assert isinstance(offset, bool)

        rect = QRectF(x-d/2, y-d/2, d-1, d-1)

        circle = QPainterPath()
        circle.addEllipse(rect)
        self.paint.setBrush(Qt.white)
        self.paint.drawPath(circle)

        sweep = 2.0 * 360.0 / resolution
        if index:
            count = 1
        else:
            count = int(resolution/2)
        if offset:
            a = sweep/4.0
        else:
            a = 0
        angle = 0
        self.paint.setPen(Qt.black)
        path = QPainterPath()
        for i in range(0, count):
            path.moveTo(QPointF(x, y))
            path.arcTo(rect, angle - a, sweep/2.0)
            path.closeSubpath()
            angle += sweep

        self.paint.setBrush(color)
        self.paint.drawPath(path)

    def paintEvent(self, event):
        if self.encoder:
            rect = event.rect()
            w = float(rect.width())
            h = float(rect.height())
            x = w/2.0
            y = h/2.0

            outside_diam = min(w, h)
            scaling = float(outside_diam / self.encoder.outer())
            inside_diam = float(self.encoder.inner() * scaling)

            self.paint = QPainter()
            self.paint.begin(self)
            self.paint.setRenderHint(QPainter.HighQualityAntialiasing)

            if isinstance(self.encoder, StandardEncoder):
                track_count = 1
                if self.encoder.quadrature():
                    track_count += 1
                if self.encoder.index():
                    track_count += 1

                diam = outside_diam
                self.draw_track(self.paint, x, y, diam, resolution=self.encoder.resolution())
                track_width = (outside_diam - inside_diam)/track_count
                res = self.encoder.resolution()

                if self.encoder.quadrature():
                    diam -= track_width
                    self.draw_track(self.paint, x, y, diam, resolution=res, offset=True)

                if self.encoder.index():
                    diam -= track_width
                    self.draw_track(self.paint, x, y, diam, resolution=res, index=True)

            elif isinstance(self.encoder, AbsoluteEncoder):
                res = self.encoder.resolution()
                track_count = 0
                while res:
                    track_count += 1
                    res >>= 1

                track_width = (outside_diam - inside_diam)/(track_count - 1)

                diam = outside_diam
                res = self.encoder.resolution()
                for t in range(1, track_count):
                    off = self.encoder.code() == AbsoluteEncoder.Gray
                    self.draw_track(self.paint, x, y, diam, resolution=res, offset=off)
                    diam -= track_width
                    res >>= 1

            self.draw_track(self.paint, x, y, inside_diam, resolution=1, color=Qt.white)
            self.paint.end()


class BinarySpinBox(QSpinBox):
    def __init__(self, parent=None):
        super(BinarySpinBox, self).__init__(parent)

    def stepBy(self, steps):
        if steps > 0:
            self.setValue(self.value() << steps)
        elif steps < 0:
            self.setValue(self.value() >> -steps)


class WheelEncoderFile(QObject):
    def __init__(self):
        super(WheelEncoderFile, self).__init__()

    def save(self, filename, encoder):
        ## TODO: Finish
        pass

    def load(self, filename):
        ## TODO: Finish
        pass


def main():
    app = QApplication(sys.argv)
    weg = WheelEncoderGeneratorApp()
    sys.exit(app.exec_())

if __name__ == '__main__':
    main()