package com;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

// ============================================================
// TEMA WARNA & FONT (UPGRADED PALETTE)
// ============================================================
class Tema {
    static final Color BG = new Color(0x07090E);
    static final Color BG2 = new Color(0x0B0F16);
    static final Color PANEL = new Color(0x101620);
    static final Color PANEL2 = new Color(0x16202E);
    static final Color GLASS = new Color(20, 30, 45, 190);
    static final Color BORDER = new Color(0x2A3444);
    static final Color BORDER_L = new Color(0x3A4658);
    static final Color TEXT = new Color(0xEAF0FA);
    static final Color TEXT_DIM = new Color(0xB0BACE);
    static final Color MUTED = new Color(0x7E8AA3);
    static final Color RED = new Color(0xE5484D);
    static final Color RED_L = new Color(0xFF6B70);
    static final Color GREEN = new Color(0x30D158);
    static final Color GREEN_L = new Color(0x5CFF8A);
    static final Color CYAN = new Color(0x22D3EE);
    static final Color CYAN_L = new Color(0x67F0FF);
    static final Color AMBER = new Color(0xFFB020);
    static final Color AMBER_L = new Color(0xFFD166);
    static final Color PURPLE = new Color(0xA78BFA);
    static final Color PURPLE_L = new Color(0xC9B8FF);
    static final Color GOLD = new Color(0xF5C542);
    static final Color TRACK = new Color(0x1E2734);
    static final Color ROAD = new Color(0x252B38);
    static final Color ROAD_D = new Color(0x1B212C);
    static final Color ROAD_LN = new Color(0xB8BEC9);
    static final Color GRASS = new Color(0x17271B);
    static final Color GRASS_L = new Color(0x1F3524);
    static final Color BUILD = new Color(0x2A3444);
    static final Color BUILD_L = new Color(0x35415A);

    static final Font F_LOGO = new Font("Segoe UI", Font.BOLD, 44);
    static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 34);
    static final Font F_H = new Font("Segoe UI", Font.BOLD, 22);
    static final Font F_H2 = new Font("Segoe UI", Font.BOLD, 16);
    static final Font F_SUB = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    static final Font F_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_MONO = new Font("Consolas", Font.PLAIN, 12);
    static final Font F_MONOB = new Font("Consolas", Font.BOLD, 12);
}

// ============================================================
// EFEK VISUAL
// ============================================================
class Fx {
    static void antialias(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    static void vignette(Graphics2D g2, int w, int h, float strength) {
        RadialGradientPaint r = new RadialGradientPaint(
                new Point2D.Float(w / 2f, h / 2f),
                Math.max(w, h) * 0.72f,
                new float[] { 0.45f, 1f },
                new Color[] { new Color(0, 0, 0, 0), new Color(0, 0, 0, (int) (strength * 255)) });
        g2.setPaint(r);
        g2.fillRect(0, 0, w, h);
    }

    static void glow(Graphics2D g2, int cx, int cy, int radius, Color c, int layers) {
        for (int i = layers; i > 0; i--) {
            float f = (float) i / layers;
            int rr = (int) (radius * f);
            int a = (int) (70 * (1 - f));
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), a));
            g2.fillOval(cx - rr, cy - rr, rr * 2, rr * 2);
        }
    }

    static void glowRect(Graphics2D g2, int x, int y, int w, int h, int radius,
            Color c, int layers) {
        for (int i = layers; i > 0; i--) {
            float f = (float) i / layers;
            int pad = (int) (radius * f * 0.6);
            int a = (int) (45 * (1 - f));
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), a));
            g2.fillRoundRect(x - pad, y - pad, w + pad * 2, h + pad * 2,
                    radius + pad, radius + pad);
        }
    }

    static void softBloom(Graphics2D g2, int w, int h) {
        g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 12),
                0, h, new Color(0, 0, 0, 0)));
        g2.fillRect(0, 0, w, h);
    }

    static void scanlines(Graphics2D g2, int w, int h, int alpha) {
        g2.setColor(new Color(0, 0, 0, alpha));
        for (int y = 0; y < h; y += 3)
            g2.fillRect(0, y, w, 1);
    }

    static void grain(Graphics2D g2, int w, int h, int alpha) {
        Random r = new Random(1234);
        g2.setColor(new Color(255, 255, 255, alpha));
        for (int i = 0; i < w * h / 900; i++) {
            g2.fillRect(r.nextInt(w), r.nextInt(h), 1, 1);
        }
    }
}

// ============================================================
// KOMPONEN KUSTOM
// ============================================================
class RoundedPanel extends JPanel {
    private final Color fill;
    private final int radius;
    private final boolean glass;

    public RoundedPanel(LayoutManager lm, Color fill, int radius) {
        this(lm, fill, radius, false);
    }

    public RoundedPanel(LayoutManager lm, Color fill, int radius, boolean glass) {
        super(lm);
        this.fill = fill;
        this.radius = radius;
        this.glass = glass;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Fx.antialias(g2);
        g2.setColor(new Color(0, 0, 0, 90));
        g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, radius, radius);
        if (glass) {
            g2.setPaint(new GradientPaint(0, 0, new Color(fill.getRed(), fill.getGreen(),
                    fill.getBlue(), Math.min(230, fill.getAlpha() + 30)),
                    0, getHeight(), fill));
        } else {
            g2.setColor(fill);
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(new Color(255, 255, 255, 22));
        g2.drawLine(radius / 2, 1, getWidth() - radius / 2, 1);
        g2.setColor(glass ? new Color(Tema.BORDER_L.getRed(), Tema.BORDER_L.getGreen(),
                Tema.BORDER_L.getBlue(), 200) : Tema.BORDER);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}

class ModernButton extends JButton {
    private final Color base;
    private boolean pulse = false;
    private float pulsePhase = 0;

    public ModernButton(String text, Color base) {
        super(text);
        this.base = base;
        setFont(Tema.F_BOLD);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(11, 18, 11, 18));
    }

    public void setPulse(boolean b) {
        pulse = b;
    }

    public void tickPulse(float dt) {
        if (pulse) {
            pulsePhase += dt * 3;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Fx.antialias(g2);
        Color c;
        if (!isEnabled())
            c = new Color(0x232B38);
        else if (getModel().isPressed())
            c = base.darker().darker();
        else if (getModel().isRollover())
            c = base.brighter();
        else
            c = base;

        if (isEnabled() && (getModel().isRollover() || pulse)) {
            Fx.glowRect(g2, 0, 0, getWidth(), getHeight(), 12, base, 4);
        }

        g2.setColor(new Color(0, 0, 0, 110));
        g2.fillRoundRect(1, 3, getWidth() - 2, getHeight() - 2, 12, 12);

        g2.setPaint(new GradientPaint(0, 0, c.brighter(), 0, getHeight(), c.darker()));
        g2.fillRoundRect(0, 0, getWidth(), getHeight() - 2, 12, 12);

        g2.setColor(new Color(255, 255, 255, 55));
        g2.drawLine(10, 2, getWidth() - 10, 2);

        g2.setColor(new Color(255, 255, 255, 40));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 3, 12, 12);
        g2.dispose();
        setForeground(isEnabled() ? Color.WHITE : new Color(0x6B7280));
        super.paintComponent(g);
    }
}

class Pill extends JLabel {
    private Color accent;
    private float phase = 0;

    public Pill(String text, Color accent) {
        super(text);
        this.accent = accent;
        setFont(Tema.F_BOLD);
        setForeground(Color.WHITE);
        setBorder(new EmptyBorder(8, 18, 8, 18));
        setOpaque(false);
    }

    public void setAccent(Color c) {
        this.accent = c;
    }

    public void tick(float dt) {
        phase += dt * 2.5f;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Fx.antialias(g2);
        Fx.glowRect(g2, 0, 0, getWidth(), getHeight(), getHeight(), accent, 4);
        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 40));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
        g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 25),
                0, getHeight(), new Color(0, 0, 0, 0)));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
        g2.setColor(accent);
        g2.setStroke(new BasicStroke(1.3f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
        int dotX = 16;
        int dotY = getHeight() / 2;
        float a = (float) (0.55 + 0.45 * Math.sin(phase));
        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), (int) (a * 255)));
        g2.fillOval(dotX - 4, dotY - 4, 8, 8);
        Fx.glow(g2, dotX, dotY, 12, accent, 3);
        g2.dispose();
        super.paintComponent(g);
    }
}

// ============================================================
// MODEL / STATE
// ============================================================
enum Scene {
    HOME, THEFT, REALIZE, REMOTE, LAPTOP, WIN
}

class Model {
    static final int WORLD_W = 1200;
    static final int WORLD_H = 720;

    Scene scene = Scene.HOME;
    int stage = 0;

    final double houseX = 140, houseY = 130;

    double carX = 200, carY = 160;
    double carAngle = 0;
    double carSpeed = 0;
    double throttle = 0, steer = 0;

    boolean doorsOpen = false;
    boolean thiefInside = true;
    boolean thiefVisible = false;
    double thiefX, thiefY, thiefVX, thiefVY;
    boolean thiefEscaping = false;

    double hackProgress = 0;
    boolean hacking = false;

    List<double[]> thiefPath = new ArrayList<>();
    int thiefWp = 0;

    List<Point2D.Double> trail = new ArrayList<>();
    int trailTick = 0;

    public Model() {
        buildThiefPath();
    }

    void buildThiefPath() {
        thiefPath.clear();
        thiefPath.add(new double[] { 200, 160 });
        thiefPath.add(new double[] { 200, 310 });
        thiefPath.add(new double[] { 530, 310 });
        thiefPath.add(new double[] { 530, 550 });
        thiefPath.add(new double[] { 930, 550 });
        thiefPath.add(new double[] { 930, 310 });
        thiefPath.add(new double[] { 900, 310 });
        thiefPath.add(new double[] { 850, 310 });
    }

    public void update(double dt) {
        if (scene != Scene.LAPTOP)
            return;

        if (thiefInside && stage == 0)
            aiDrive();

        // ✅ FIX: kontrol pemain aktif saat stage == 5 (setelah klik KONTROL MOBIL)
        if (!thiefInside && stage == 5) {
            double accel = throttle * 0.35;
            carSpeed += accel;
            carSpeed *= (throttle == 0) ? 0.965 : 0.995;
            carSpeed = Math.max(-2.5, Math.min(5.0, carSpeed));
            if (Math.abs(carSpeed) < 0.05)
                carSpeed = 0;
            if (steer != 0) {
                double f = Math.max(0.35, Math.min(1.0, Math.abs(carSpeed) / 5.0 + 0.2));
                carAngle += steer * 3.0 * f * (carSpeed < -0.05 ? -1 : 1);
            }
        } else if (!thiefInside) {
            carSpeed *= 0.95;
            if (Math.abs(carSpeed) < 0.05)
                carSpeed = 0;
        } else if (stage > 0) {
            carSpeed *= 0.97;
            if (Math.abs(carSpeed) < 0.05)
                carSpeed = 0;
        }

        double rad = Math.toRadians(carAngle);
        double nextX = carX + carSpeed * Math.cos(rad);
        double nextY = carY + carSpeed * Math.sin(rad);
        boolean nabrak = false;

        double m = 25;
        if (nextX < m || nextX > 1175 || nextY < m || nextY > 695)
            nabrak = true;

        if (nextX > 100 && nextX < 180 && nextY > 90 && nextY < 170)
            nabrak = true;

        if (nextX > 30 && nextX < 270 && nextY > 530 && nextY < 720)
            nabrak = true;

        java.util.Random r = new java.util.Random(42);
        int[] koordinatX = { 60, 260, 600, 750, 1000 };
        int[] koordinatY = { 60, 370, 610 };
        for (int bx : koordinatX) {
            for (int by : koordinatY) {
                if (bx < 300 && by < 300)
                    continue;
                if (bx < 300 && by > 500)
                    continue;
                int bw = 100 + r.nextInt(30);
                int bh = 100 + r.nextInt(30);
                if (nextX > bx && nextX < bx + bw && nextY > by && nextY < by + bh) {
                    nabrak = true;
                }
            }
        }

        if (nabrak) {
            carSpeed = carSpeed * -0.4;
        } else {
            carX = nextX;
            carY = nextY;
        }

        carAngle = ((carAngle % 360) + 360) % 360;

        trailTick++;
        if (trailTick % 3 == 0 && Math.abs(carSpeed) > 0.2) {
            trail.add(new Point2D.Double(carX, carY));
            if (trail.size() > 300)
                trail.remove(0);
        }

        if (thiefEscaping) {
            thiefX += thiefVX * dt;
            thiefY += thiefVY * dt;
            if (thiefX < -120 || thiefX > WORLD_W + 120 ||
                    thiefY < -120 || thiefY > WORLD_H + 120) {
                thiefEscaping = false;
                thiefVisible = false;
            }
        }
    }

    private void aiDrive() {
        if (thiefWp >= thiefPath.size())
            return;
        double[] t = thiefPath.get(thiefWp);
        double dx = t[0] - carX, dy = t[1] - carY;
        double d = Math.hypot(dx, dy);
        if (d < 15) {
            if (thiefWp < thiefPath.size() - 1)
                thiefWp++;
            return;
        }
        double tAng = Math.toDegrees(Math.atan2(dy, dx));
        if (tAng < 0)
            tAng += 360;
        double diff = tAng - carAngle;
        while (diff > 180)
            diff -= 360;
        while (diff < -180)
            diff += 360;
        carAngle += Math.signum(diff) * Math.min(Math.abs(diff), 4);
        carSpeed = Math.min(carSpeed + 0.12, 2.6);
    }

    public void ejectThief() {
        if (!thiefInside)
            return;
        thiefInside = false;
        thiefVisible = true;
        thiefX = carX + 40;
        thiefY = carY + 20;
        thiefEscaping = true;
        double a = Math.random() * Math.PI * 2;
        thiefVX = Math.cos(a) * 200;
        thiefVY = Math.sin(a) * 200;
    }

    public void reset() {
        scene = Scene.HOME;
        stage = 0;
        carX = 200;
        carY = 160;
        carAngle = 0;
        carSpeed = 0;
        throttle = 0;
        steer = 0;
        doorsOpen = false;
        thiefInside = true;
        thiefVisible = false;
        thiefEscaping = false;
        hackProgress = 0;
        hacking = false;
        thiefWp = 0;
        trail.clear();
    }
}

interface SceneLifecycle {
    void onEnter();
}

// ============================================================
// DRAWING TOOLKIT
// ============================================================
class Draw {
    static void antialias(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    static void text(Graphics2D g2, String s, int cx, int y, Font f, Color c) {
        g2.setFont(f);
        g2.setColor(c);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(s, cx - fm.stringWidth(s) / 2, y);
    }

    static void textGlow(Graphics2D g2, String s, int cx, int y, Font f, Color c, Color glow) {
        g2.setFont(f);
        FontMetrics fm = g2.getFontMetrics();
        int x = cx - fm.stringWidth(s) / 2;
        for (int i = 3; i > 0; i--) {
            g2.setColor(new Color(glow.getRed(), glow.getGreen(), glow.getBlue(), 40 / i));
            g2.drawString(s, x - i, y);
            g2.drawString(s, x + i, y);
            g2.drawString(s, x, y - i);
            g2.drawString(s, x, y + i);
        }
        g2.setColor(c);
        g2.drawString(s, x, y);
    }

    static void house(Graphics2D g2, double x, double y, double s) {
        g2.setColor(new Color(0x1B3322));
        g2.fillRoundRect((int) (x - s - 22), (int) (y - s - 22), (int) (s * 2 + 44), (int) (s * 2 + 44), 26, 26);
        g2.setColor(new Color(0x2A4A32));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect((int) (x - s - 22), (int) (y - s - 22), (int) (s * 2 + 44), (int) (s * 2 + 44), 26, 26);
        tree(g2, x - s - 40, y + s - 6, 12);
        tree(g2, x + s + 40, y - s + 6, 12);

        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect((int) (x - s + 2), (int) (y - s + 6), (int) (s * 2), (int) (s * 2), 14, 14);

        g2.setPaint(new GradientPaint((float) x, (float) (y - s), new Color(0xE8C79A),
                (float) x, (float) (y + s), new Color(0x8B6F47)));
        g2.fillRoundRect((int) (x - s), (int) (y - s), (int) (s * 2), (int) (s * 2), 14, 14);

        g2.setColor(new Color(0, 0, 0, 25));
        for (int i = 0; i < s * 2; i += 8) {
            g2.drawLine((int) (x - s), (int) (y - s) + i, (int) (x + s), (int) (y - s) + i);
        }

        g2.setColor(new Color(0, 0, 0, 100));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect((int) (x - s), (int) (y - s), (int) (s * 2), (int) (s * 2), 14, 14);

        int[] rx = { (int) (x - s - 8), (int) (x + s + 8), (int) (x + s + 2), (int) (x - s - 2) };
        int[] ry = { (int) (y - s - 4), (int) (y - s - 4), (int) (y - s + 10), (int) (y - s + 10) };
        g2.setColor(new Color(0x5A1E1E));
        g2.fillPolygon(rx, ry, 4);
        int[] rx2 = { (int) (x - s - 8), (int) (x + s + 8), (int) (x + s + 4), (int) (x - s - 4) };
        int[] ry2 = { (int) (y - s - 4), (int) (y - s - 4), (int) (y - s + 2), (int) (y - s + 2) };
        g2.setColor(new Color(0x8B2B2B));
        g2.fillPolygon(rx2, ry2, 4);
        g2.setColor(new Color(0xC13B3B));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine((int) (x - s - 8), (int) (y - s - 4), (int) (x + s + 8), (int) (y - s - 4));

        g2.setColor(new Color(0x4A1818));
        g2.fillRect((int) (x + s - 20), (int) (y - s - 14), 12, 14);

        g2.setColor(new Color(0x3B2412));
        g2.fillRoundRect((int) (x - 10), (int) (y + s - 14), 20, 14, 4, 4);
        g2.setColor(new Color(0x5C3B1E));
        g2.fillRoundRect((int) (x - 8), (int) (y + s - 12), 16, 12, 3, 3);
        g2.setColor(Tema.GOLD);
        g2.fillOval((int) (x + 3), (int) (y + s - 6), 3, 3);

        drawWindow(g2, (int) (x - s + 8), (int) (y - 6), 12, 12);
        drawWindow(g2, (int) (x + s - 20), (int) (y - 6), 12, 12);
        drawWindow(g2, (int) (x - s + 8), (int) (y + s - 32), 12, 12);

        textGlow(g2, "RUMAH", (int) x, (int) (y - s - 30), Tema.F_H2, Tema.TEXT, Tema.GREEN);
    }

    static void drawWindow(Graphics2D g2, int x, int y, int w, int h) {
        Fx.glow(g2, x + w / 2, y + h / 2, w * 2, Tema.AMBER_L, 3);
        g2.setColor(new Color(0x2A1A0A));
        g2.fillRoundRect(x - 2, y - 2, w + 4, h + 4, 4, 4);
        g2.setColor(new Color(0xFFE9A8));
        g2.fillRect(x, y, w, h);
        g2.setColor(new Color(0x2A1A0A));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(x + w / 2, y, x + w / 2, y + h);
        g2.drawLine(x, y + h / 2, x + w, y + h / 2);
    }

    static void tree(Graphics2D g2, double x, double y, double r) {
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillOval((int) (x - r), (int) (y + r * 0.6), (int) (r * 2), (int) (r * 0.6));
        g2.setColor(new Color(0x1F3A24));
        g2.fillOval((int) (x - r), (int) (y - r), (int) (r * 2), (int) (r * 2));
        g2.setColor(new Color(0x2E5734));
        g2.fillOval((int) (x - r * 0.7), (int) (y - r * 0.9), (int) (r * 1.2), (int) (r * 1.2));
        g2.setColor(new Color(0x3E6B42));
        g2.fillOval((int) (x - r * 0.4), (int) (y - r * 0.7), (int) (r * 0.7), (int) (r * 0.7));
    }

    static void car(Graphics2D g2, double cx, double cy, double angle,
            Color body, boolean doorsOpen, boolean thiefInside, boolean headlight) {
        Graphics2D gc = (Graphics2D) g2.create();
        antialias(gc);
        gc.translate(cx, cy);
        gc.rotate(Math.toRadians(angle));

        for (int i = 3; i >= 1; i--) {
            gc.setColor(new Color(0, 0, 0, 40 / i));
            gc.fillRoundRect(-26 - i, -10 - i, 52 + i * 2, 26 + i * 2, 14, 14);
        }
        gc.setColor(new Color(0, 0, 0, 120));
        gc.fillRoundRect(-24, -8, 50, 22, 12, 12);

        if (headlight) {
            gc.setPaint(new GradientPaint(24, 0, new Color(255, 240, 190, 90),
                    130, 0, new Color(255, 240, 190, 0)));
            Polygon cone = new Polygon();
            cone.addPoint(24, -8);
            cone.addPoint(140, -50);
            cone.addPoint(140, 50);
            cone.addPoint(24, 8);
            gc.fillPolygon(cone);
            gc.setPaint(new GradientPaint(24, 0, new Color(255, 255, 220, 130),
                    90, 0, new Color(255, 255, 220, 0)));
            Polygon core = new Polygon();
            core.addPoint(24, -5);
            core.addPoint(90, -22);
            core.addPoint(90, 22);
            core.addPoint(24, 5);
            gc.fillPolygon(core);
        }

        gc.setColor(new Color(0, 0, 0, 90));
        gc.fillRoundRect(-24, 6, 48, 9, 8, 8);

        gc.setPaint(new LinearGradientPaint(
                new Point2D.Float(0, -14),
                new Point2D.Float(0, 14),
                new float[] { 0f, 0.5f, 1f },
                new Color[] { body.brighter().brighter(), body, body.darker().darker() }));
        gc.fillRoundRect(-24, -14, 48, 28, 13, 13);

        gc.setColor(new Color(255, 255, 255, 70));
        gc.setStroke(new BasicStroke(1.4f));
        gc.drawArc(-22, -13, 44, 12, 10, 160);

        gc.setColor(new Color(200, 220, 255, 60));
        gc.setStroke(new BasicStroke(1));
        gc.drawLine(-20, -8, 20, -8);
        gc.drawLine(-20, 8, 20, 8);

        gc.setColor(new Color(0, 0, 0, 120));
        gc.setStroke(new BasicStroke(1.6f));
        gc.drawRoundRect(-24, -14, 48, 28, 13, 13);

        gc.setPaint(new GradientPaint(11, -8, new Color(0xDAF0FF),
                11, 8, new Color(0x6EA8D0)));
        gc.fillRoundRect(10, -8, 11, 16, 5, 5);
        gc.setColor(new Color(255, 255, 255, 120));
        gc.fillPolygon(new int[] { 12, 16, 14, 12 }, new int[] { -6, -6, -2, -2 }, 4);
        gc.setPaint(new GradientPaint(-16, -7, new Color(0xBFE0F5),
                -16, 7, new Color(0x6A9FC0)));
        gc.fillRoundRect(-20, -7, 8, 14, 4, 4);

        gc.setColor(new Color(255, 255, 255, 50));
        gc.fillRect(-9, -2, 18, 3);
        gc.setColor(new Color(0, 0, 0, 50));
        gc.fillRect(-9, 1, 18, 1);

        if (doorsOpen) {
            gc.setColor(new Color(255, 255, 255, 230));
            gc.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            gc.drawLine(-4, -14, -4, -25);
            gc.drawLine(-4, 14, -4, 25);
            gc.setColor(new Color(255, 255, 255, 120));
            gc.setStroke(new BasicStroke(1.5f));
            gc.drawOval(-7, -27, 6, 4);
            gc.drawOval(-7, 23, 6, 4);
        }

        wheel(gc, -16, -17);
        wheel(gc, 8, -17);
        wheel(gc, -16, 12);
        wheel(gc, 8, 12);

        gc.setColor(new Color(0xFFF4C2));
        gc.fillRoundRect(20, -9, 4, 5, 2, 2);
        gc.fillRoundRect(20, 4, 4, 5, 2, 2);
        if (headlight) {
            Fx.glow(gc, 22, -7, 14, Tema.AMBER_L, 3);
            Fx.glow(gc, 22, 6, 14, Tema.AMBER_L, 3);
        }
        gc.setColor(new Color(0xE5484D));
        gc.fillRoundRect(-24, -8, 3, 5, 2, 2);
        gc.fillRoundRect(-24, 3, 3, 5, 2, 2);
        Fx.glow(gc, -22, -6, 10, Tema.RED, 3);
        Fx.glow(gc, -22, 5, 10, Tema.RED, 3);

        if (thiefInside) {
            gc.setColor(new Color(0x14181F));
            gc.fillOval(-5, -6, 11, 11);
            gc.setColor(new Color(0x0A0D12));
            gc.fillArc(-6, -8, 13, 8, 0, 180);
            gc.setColor(new Color(0xE5484D));
            gc.fillRect(-5, -1, 11, 2);
            gc.setColor(new Color(0xF2C9A0));
            gc.fillOval(6, -3, 4, 4);
            gc.fillOval(6, 0, 4, 4);
        }
        gc.dispose();
    }

    static void wheel(Graphics2D gc, int x, int y) {
        gc.setColor(new Color(0x08090C));
        gc.fillRoundRect(x, y, 11, 6, 3, 3);
        gc.setColor(new Color(0x6A7284));
        gc.fillRect(x + 2, y + 1, 7, 4);
        gc.setColor(new Color(0xB0B8C8));
        gc.fillRect(x + 3, y + 2, 5, 1);
    }

    static void thiefTop(Graphics2D g2, double x, double y) {
        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillOval((int) x - 11, (int) y + 7, 22, 7);
        Fx.glow(g2, (int) x, (int) y, 26, Tema.RED, 4);
        g2.setPaint(new RadialGradientPaint(new Point2D.Double(x, y),
                14, new float[] { 0f, 1f },
                new Color[] { new Color(0x2A2F3A), new Color(0x10141A) }));
        g2.fillOval((int) x - 11, (int) y - 11, 22, 22);
        g2.setColor(new Color(0x0A0D12));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval((int) x - 11, (int) y - 11, 22, 22);
        g2.setColor(new Color(0x0A0D12));
        g2.fillArc((int) x - 10, (int) y - 12, 20, 14, 0, 180);
        g2.setColor(Tema.RED_L);
        g2.fillRect((int) x - 8, (int) y - 4, 16, 3);
        Fx.glow(g2, (int) x, (int) y - 3, 10, Tema.RED, 2);
    }

    static void thiefSide(Graphics2D g2, double x, double y, double scale, double phase) {
        Graphics2D gc = (Graphics2D) g2.create();
        antialias(gc);
        gc.translate(x, y);
        gc.scale(scale, scale);

        gc.setColor(new Color(0, 0, 0, 150));
        gc.fillOval(-24, 28, 48, 11);

        double legSwing = Math.sin(phase) * 8;

        gc.setColor(new Color(0x14181F));
        gc.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gc.drawLine(-4, 12, (int) (-8 + legSwing), 26);
        gc.drawLine(4, 12, (int) (8 - legSwing), 26);
        gc.setColor(new Color(0x0A0D12));
        gc.fillOval((int) (-12 + legSwing), 24, 10, 6);
        gc.fillOval((int) (4 - legSwing), 24, 10, 6);

        gc.setPaint(new GradientPaint(0, -8, new Color(0x353A47), 0, 16, new Color(0x181D26)));
        gc.fillRoundRect(-11, -8, 22, 26, 8, 8);
        gc.setColor(new Color(0x0A0D12));
        gc.setStroke(new BasicStroke(1.5f));
        gc.drawLine(0, -8, 0, 18);
        gc.setColor(Tema.RED);
        gc.fillRect(-8, -6, 16, 2);

        gc.setColor(new Color(0x1A1F27));
        gc.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gc.drawLine(-9, -3, (int) (-15 - legSwing), 10);
        gc.drawLine(9, -3, (int) (15 + legSwing), 10);
        gc.setColor(new Color(0x0A0D12));
        gc.fillOval((int) (-20 - legSwing), 8, 7, 7);
        gc.fillOval((int) (11 + legSwing), 8, 7, 7);

        gc.setColor(new Color(0xF2C9A0));
        gc.fillOval(-9, -25, 18, 18);
        gc.setColor(new Color(0, 0, 0, 60));
        gc.fillArc(-9, -25, 18, 12, 0, 180);
        gc.setColor(new Color(0x0A0D12));
        gc.fillArc(-11, -30, 22, 17, 0, 180);
        gc.fillRect(-13, -15, 26, 3);
        gc.setColor(Tema.RED);
        gc.fillRect(-9, -15, 18, 4);
        gc.setColor(new Color(0xFFFFFF));
        gc.fillRect(-6, -12, 4, 2);
        gc.fillRect(2, -12, 4, 2);
        gc.setColor(new Color(0x0A0D12));
        gc.fillRect(-5, -12, 2, 2);
        gc.fillRect(3, -12, 2, 2);

        gc.dispose();
    }

    static void player(Graphics2D g2, double x, double y, double scale) {
        Graphics2D gc = (Graphics2D) g2.create();
        antialias(gc);
        gc.translate(x, y);
        gc.scale(scale, scale);

        double bob = Math.sin(System.currentTimeMillis() / 500.0) * 1.5;

        gc.setColor(new Color(0, 0, 0, 150));
        gc.fillOval(-24, 28, 48, 11);

        gc.setColor(new Color(0x2A3140));
        gc.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gc.drawLine(-4, 12, -6, 27);
        gc.drawLine(4, 12, 6, 27);
        gc.setColor(new Color(0x14181F));
        gc.fillOval(-11, 25, 11, 6);
        gc.fillOval(2, 25, 11, 6);

        gc.setPaint(new GradientPaint(0, -8 + (int) bob, new Color(0x5F82C2),
                0, 16 + (int) bob, new Color(0x2F4A80)));
        gc.fillRoundRect(-12, -8 + (int) bob, 24, 26, 9, 9);
        gc.setColor(new Color(0x1A1F27));
        gc.fillRect(-12, 12 + (int) bob, 24, 4);
        gc.setColor(Tema.GOLD);
        gc.fillRect(-2, 13 + (int) bob, 4, 2);

        gc.setColor(new Color(0x3E5FA0));
        gc.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gc.drawLine(-11, -3 + (int) bob, -17, 10 + (int) bob);
        gc.drawLine(11, -3 + (int) bob, 17, 10 + (int) bob);
        gc.setColor(new Color(0xF2C9A0));
        gc.fillOval(-21, 8 + (int) bob, 7, 7);
        gc.fillOval(14, 8 + (int) bob, 7, 7);

        gc.setColor(new Color(0xF2C9A0));
        gc.fillOval(-9, -25 + (int) bob, 18, 18);
        gc.setColor(new Color(0x3A2410));
        gc.fillArc(-10, -30 + (int) bob, 20, 15, 0, 180);
        gc.setColor(new Color(0x0A0D12));
        gc.fillRect(-5, -17 + (int) bob, 2, 2);
        gc.fillRect(3, -17 + (int) bob, 2, 2);

        gc.dispose();
    }

    static void cityMap(Graphics2D g2, int w, int h) {
        g2.setPaint(new GradientPaint(0, 0, Tema.GRASS_L, w, h, Tema.GRASS));
        g2.fillRect(0, 0, w, h);

        java.util.Random r = new java.util.Random(42);
        for (int i = 0; i < 200; i++) {
            int x = r.nextInt(w), y = r.nextInt(h);
            g2.setColor(new Color(0x1F3524));
            g2.fillRect(x, y, 3, 2);
        }

        drawPark(g2, 40, h - 180, 220, 140, r);

        int[] koordinatX = { 60, 260, 600, 750, 1000 };
        int[] koordinatY = { 60, 370, 610 };

        for (int bx : koordinatX) {
            for (int by : koordinatY) {
                if (bx < 300 && by < 300)
                    continue;
                if (bx < 300 && by > 500)
                    continue;
                int bw = 100 + r.nextInt(30);
                int bh = 100 + r.nextInt(30);
                drawBuilding(g2, bx, by, bw, bh, r);
            }
        }

        drawRoadH(g2, 280, w);
        drawRoadH(g2, 520, w);
        drawRoadV(g2, 500, h);
        drawRoadV(g2, 900, h);
    }

    static void drawRoadH(Graphics2D g2, int y, int w) {
        g2.setColor(Tema.ROAD_D);
        g2.fillRect(0, y - 2, w, 64);
        g2.setColor(Tema.ROAD);
        g2.fillRect(0, y, w, 60);
        g2.setColor(new Color(0x3A404D));
        g2.fillRect(0, y - 4, w, 4);
        g2.fillRect(0, y + 60, w, 4);
        g2.setColor(new Color(0xE5B948));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1f, new float[] { 20, 16 }, 0));
        g2.drawLine(0, y + 30, w, y + 30);
        g2.setColor(new Color(0xD0D4DC));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawLine(0, y + 6, w, y + 6);
        g2.drawLine(0, y + 54, w, y + 54);
    }

    static void drawRoadV(Graphics2D g2, int x, int h) {
        g2.setColor(Tema.ROAD_D);
        g2.fillRect(x - 2, 0, 64, h);
        g2.setColor(Tema.ROAD);
        g2.fillRect(x, 0, 60, h);
        g2.setColor(new Color(0x3A404D));
        g2.fillRect(x - 4, 0, 4, h);
        g2.fillRect(x + 60, 0, 4, h);
        g2.setColor(new Color(0xE5B948));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1f, new float[] { 20, 16 }, 0));
        g2.drawLine(x + 30, 0, x + 30, h);
        g2.setColor(new Color(0xD0D4DC));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawLine(x + 6, 0, x + 6, h);
        g2.drawLine(x + 54, 0, x + 54, h);
    }

    static void drawBuilding(Graphics2D g2, int x, int y, int w, int h, Random r) {
        g2.setColor(new Color(0, 0, 0, 110));
        g2.fillRoundRect(x + 3, y + 5, w, h, 8, 8);
        g2.setPaint(new GradientPaint(x, y, Tema.BUILD_L, x, y + h, Tema.BUILD));
        g2.fillRoundRect(x, y, w, h, 8, 8);
        g2.setColor(new Color(0x0E131B));
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawRoundRect(x, y, w, h, 8, 8);
        g2.setColor(new Color(0x1A2130));
        g2.fillRoundRect(x + 4, y + 4, w - 8, 6, 3, 3);
        int cols = Math.max(2, w / 22);
        int rows = Math.max(2, h / 22);
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int wx = x + 8 + i * ((w - 16) / cols);
                int wy = y + 16 + j * ((h - 26) / rows);
                int ww = (w - 16) / cols - 4;
                int wh = (h - 26) / rows - 4;
                if (ww <= 0 || wh <= 0)
                    continue;
                if (r.nextInt(3) == 0) {
                    g2.setColor(new Color(0xFFE9A8));
                } else {
                    g2.setColor(new Color(0x1A2130));
                }
                g2.fillRect(wx, wy, ww, wh);
                g2.setColor(new Color(0x0E131B));
                g2.drawRect(wx, wy, ww, wh);
            }
        }
    }

    static void drawPark(Graphics2D g2, int x, int y, int w, int h, Random r) {
        g2.setColor(new Color(0x2E5734));
        g2.fillRoundRect(x, y, w, h, 14, 14);
        g2.setColor(new Color(0x1F3A24));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(x, y, w, h, 14, 14);
        g2.setColor(new Color(0x2A5A7A));
        g2.fillOval(x + 30, y + 30, 70, 50);
        g2.setColor(new Color(0x3E7A9E));
        g2.fillOval(x + 35, y + 35, 60, 40);
        for (int i = 0; i < 6; i++) {
            int tx = x + 20 + r.nextInt(w - 40);
            int ty = y + 20 + r.nextInt(h - 40);
            tree(g2, tx, ty, 10);
        }
    }
}

// ============================================================
// SCENE 1: HOME
// ============================================================
class HomePanel extends JPanel implements SceneLifecycle {
    private float glowPhase = 0;
    private final javax.swing.Timer anim;

    public HomePanel(Simulator app, Model model) {
        setBackground(Tema.BG);
        setLayout(new BorderLayout());

        JPanel titleWrap = new JPanel();
        titleWrap.setOpaque(false);
        titleWrap.setLayout(new BoxLayout(titleWrap, BoxLayout.Y_AXIS));
        titleWrap.setBorder(new EmptyBorder(80, 0, 0, 0));

        JLabel brand = new JLabel("SMART CAR SECURITY");
        brand.setFont(Tema.F_LOGO);
        brand.setForeground(Tema.TEXT);
        brand.setAlignmentX(CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("SISTEM PENGAMAN MOBIL PINTAR — SIMULASI INTERAKTIF");
        tagline.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tagline.setForeground(Tema.CYAN);
        tagline.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Amankan kembali mobil Anda dari tangan pencuri dengan teknologi telematika.");
        sub.setFont(Tema.F_SUB);
        sub.setForeground(Tema.MUTED);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        titleWrap.add(brand);
        titleWrap.add(Box.createVerticalStrut(10));
        titleWrap.add(tagline);
        titleWrap.add(Box.createVerticalStrut(18));
        titleWrap.add(sub);

        JPanel btnWrap = new JPanel();
        btnWrap.setOpaque(false);
        btnWrap.setBorder(new EmptyBorder(0, 0, 90, 0));
        ModernButton start = new ModernButton("   START SIMULASI", Tema.GREEN.darker());
        start.setFont(new Font("Segoe UI", Font.BOLD, 17));
        start.setPreferredSize(new Dimension(280, 60));
        start.setPulse(true);
        start.addActionListener(e -> app.showScene("THEFT"));
        btnWrap.add(start);

        add(titleWrap, BorderLayout.NORTH);
        add(btnWrap, BorderLayout.SOUTH);

        anim = new javax.swing.Timer(30, e -> {
            glowPhase += 0.03f;
            repaint();
        });
        anim.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    public void removeNotify() {
        if (anim != null)
            anim.stop();
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Draw.antialias(g2);
        int w = getWidth(), h = getHeight();

        g2.setPaint(new RadialGradientPaint(new Point2D.Float(w / 2f, h * 0.35f),
                Math.max(w, h) * 0.9f, new float[] { 0f, 1f },
                new Color[] { new Color(0x152238), Tema.BG }));
        g2.fillRect(0, 0, w, h);

        g2.setColor(new Color(0x22, 0xD3, 0xEE, 12));
        for (int x = 0; x < w; x += 40)
            g2.drawLine(x, 0, x, h);
        for (int y = 0; y < h; y += 40)
            g2.drawLine(0, y, w, y);

        Random r = new Random(99);
        long t = System.currentTimeMillis();
        for (int i = 0; i < 40; i++) {
            double px = (r.nextInt(w) + t * 0.02 * (0.5 + r.nextDouble())) % w;
            double py = (r.nextInt(h) + t * 0.03 * (0.5 + r.nextDouble())) % h;
            int alpha = 30 + r.nextInt(70);
            g2.setColor(new Color(0x22, 0xD3, 0xEE, alpha));
            g2.fillOval((int) px, (int) py, 2, 2);
        }

        Fx.scanlines(g2, w, h, 4);
        Fx.vignette(g2, w, h, 0.55f);
    }

    @Override
    public void onEnter() {
    }
}

// ============================================================
// SCENE 2: THEFT
// ============================================================
class TheftPanel extends JPanel implements SceneLifecycle {
    private final Simulator app;
    private long t0;
    private int phase = 0;
    private javax.swing.Timer timer;

    public TheftPanel(Simulator app, Model model) {
        this.app = app;
        setBackground(Tema.BG);
    }

    @Override
    public void onEnter() {
        t0 = System.currentTimeMillis();
        phase = 0;
        if (timer != null)
            timer.stop();
        timer = new javax.swing.Timer(30, e -> {
            long el = System.currentTimeMillis() - t0;
            if (el < 2200)
                phase = 0;
            else if (el < 3400)
                phase = 1;
            else
                phase = 2;
            if (el > 6000) {
                ((javax.swing.Timer) e.getSource()).stop();
                app.showScene("REALIZE");
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Draw.antialias(g2);
        int w = getWidth(), h = getHeight();

        g2.setPaint(new GradientPaint(0, 0, new Color(0x081020), 0, h * 0.7f, new Color(0x02030A)));
        g2.fillRect(0, 0, w, h);

        Random cr = new Random(5);
        for (int i = 0; i < 8; i++) {
            int cx = cr.nextInt(w);
            int cy = 60 + cr.nextInt(180);
            int cw = 120 + cr.nextInt(200);
            g2.setColor(new Color(20, 30, 55, 40));
            g2.fillOval(cx, cy, cw, 40);
        }

        Random r = new Random(7);
        long t = System.currentTimeMillis();
        for (int i = 0; i < 90; i++) {
            int sx = r.nextInt(w), sy = r.nextInt(h / 2);
            int a = 100 + r.nextInt(155);
            double twinkle = 0.6 + 0.4 * Math.sin(t / 300.0 + i);
            g2.setColor(new Color(255, 255, 255, (int) (a * twinkle)));
            g2.fillRect(sx, sy, 2, 2);
        }

        int mx = w - 210, my = 110;
        Fx.glow(g2, mx, my, 130, new Color(0xC8D8F0), 5);
        g2.setColor(new Color(0xE8ECF5));
        g2.fillOval(mx - 35, my - 35, 70, 70);
        g2.setColor(new Color(0xD0D8E8));
        g2.fillOval(mx - 20, my - 15, 14, 14);
        g2.fillOval(mx + 5, my + 5, 10, 10);
        g2.fillOval(mx - 10, my + 15, 8, 8);
        g2.setColor(new Color(200, 215, 240, 30));
        g2.fillOval(mx - 55, my - 55, 110, 110);

        g2.setColor(new Color(0x0E1117));
        g2.fillRect(0, h - 200, w, 200);
        g2.setColor(new Color(0x252B38));
        g2.fillRect(0, h - 200, w, 4);
        g2.setColor(new Color(0xFFEB99));
        for (int x = 20; x < w; x += 60)
            g2.fillRect(x, h - 100, 30, 3);
        for (int lx = 80; lx < w; lx += 320) {
            drawStreetLamp(g2, lx, h - 200);
        }

        Draw.house(g2, 260, h - 280, 70);

        double carX = 430, carY = h - 155;
        long el = System.currentTimeMillis() - t0;

        if (phase == 0) {
            double p = Math.min(1, el / 2200.0);
            double tx = w + 80 - (w + 80 - (carX - 30)) * p;
            Draw.thiefSide(g2, tx, h - 195, 1.3, p * 25);
        } else if (phase == 1) {
            double p = (el - 2200) / 1200.0;
            int alpha = (int) (255 * (1 - Math.min(1, p)));
            Graphics2D gc = (Graphics2D) g2.create();
            gc.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
            Draw.thiefSide(gc, carX - 30, h - 195, 1.3, 0);
            gc.dispose();
        }

        double driveOffset = 0;
        if (phase == 2) {
            double p = (el - 3400) / 2400.0;
            driveOffset = p * (w + 300);
        }
        if (phase >= 1) {
            Fx.glow(g2, (int) (carX + driveOffset), (int) carY, 90, Tema.RED, 4);
        }
        Draw.car(g2, carX + driveOffset, carY, 0,
                phase >= 1 ? Tema.RED : new Color(0x3E5FA0),
                false, phase >= 1, true);

        String dialog;
        if (phase == 0)
            dialog = "Malam yang tenang... tiba-tiba muncul seseorang!";
        else if (phase == 1)
            dialog = "\"Hehehe... mobil ini milikku sekarang!\"";
        else
            dialog = "Pencuri membawa mobil pergi!";

        int dw = 640, dh = 70;
        int dx = w / 2 - dw / 2, dy = 44;
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(dx + 4, dy + 6, dw, dh, 20, 20);
        g2.setColor(new Color(8, 12, 20, 220));
        g2.fillRoundRect(dx, dy, dw, dh, 20, 20);
        Color accent = phase == 1 ? Tema.RED : Tema.AMBER;
        g2.setColor(accent);
        g2.setStroke(new BasicStroke(1.6f));
        g2.drawRoundRect(dx, dy, dw, dh, 20, 20);
        Fx.glowRect(g2, dx, dy, dw, dh, 20, accent, 3);
        Draw.text(g2, dialog, w / 2, dy + 44, Tema.F_H, Tema.TEXT);

        Fx.scanlines(g2, w, h, 6);
        Fx.vignette(g2, w, h, 0.7f);
    }

    private void drawStreetLamp(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(0x1F2634));
        g2.fillRect(x, y, 4, 100);
        g2.setColor(new Color(0x2A3444));
        g2.fillRoundRect(x - 10, y - 8, 24, 8, 4, 4);
        Fx.glow(g2, x + 2, y - 2, 55, new Color(0xFFE9A8), 4);
        g2.setColor(new Color(0xFFE9A8));
        g2.fillOval(x - 2, y - 2, 8, 6);
        g2.setPaint(new GradientPaint(x + 2, y, new Color(255, 233, 168, 40),
                x + 2, y + 100, new Color(255, 233, 168, 0)));
        Polygon cone = new Polygon();
        cone.addPoint(x - 4, y);
        cone.addPoint(x + 8, y);
        cone.addPoint(x + 30, y + 100);
        cone.addPoint(x - 26, y + 100);
        g2.fillPolygon(cone);
    }
}

// ============================================================
// SCENE 3: REALIZE
// ============================================================
class RealizePanel extends JPanel implements SceneLifecycle {
    private final Simulator app;
    private long t0;
    private javax.swing.Timer timer;

    public RealizePanel(Simulator app, Model model) {
        this.app = app;
        setBackground(Tema.BG);
    }

    @Override
    public void onEnter() {
        t0 = System.currentTimeMillis();
        if (timer != null)
            timer.stop();
        timer = new javax.swing.Timer(30, e -> {
            long el = System.currentTimeMillis() - t0;
            if (el > 5200) {
                ((javax.swing.Timer) e.getSource()).stop();
                app.showScene("REMOTE");
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Draw.antialias(g2);
        int w = getWidth(), h = getHeight();

        g2.setPaint(new GradientPaint(0, 0, new Color(0x232838), 0, h * 0.6f, new Color(0x0F131B)));
        g2.fillRect(0, 0, w, h);

        g2.setColor(new Color(255, 255, 255, 5));
        for (int x = 0; x < w; x += 30)
            g2.drawLine(x, 0, x, h);

        g2.setColor(new Color(0x0A0C12));
        g2.fillRect(0, h - 200, w, 200);
        g2.setColor(new Color(0x2A3140));
        g2.fillRect(0, h - 200, w, 4);
        g2.setColor(new Color(0x151922));
        for (int x = -100; x < w + 100; x += 50) {
            g2.drawLine(x, h - 200, x + 40, h);
        }
        g2.setPaint(new GradientPaint(0, h - 200, new Color(0x22, 0xD3, 0xEE, 20),
                0, h, new Color(0x22, 0xD3, 0xEE, 0)));
        g2.fillRect(0, h - 200, w, 200);

        int jx = 160, jy = 100, jw = 280, jh = 200;
        g2.setColor(new Color(0x1A1F27));
        g2.fillRoundRect(jx - 8, jy - 8, jw + 16, jh + 16, 8, 8);
        g2.setPaint(new GradientPaint(0, jy, new Color(0x0A1528), 0, jy + jh, new Color(0x02040A)));
        g2.fillRect(jx, jy, jw, jh);
        Random r = new Random(31);
        g2.setColor(new Color(255, 255, 255, 180));
        for (int i = 0; i < 30; i++) {
            g2.fillRect(jx + r.nextInt(jw), jy + r.nextInt(jh / 2), 2, 2);
        }
        Fx.glow(g2, jx + jw - 60, jy + 60, 60, new Color(0xC8D8F0), 4);
        g2.setColor(new Color(0xE8ECF5));
        g2.fillOval(jx + jw - 75, jy + 45, 30, 30);
        g2.setColor(new Color(0x060810));
        g2.fillRect(jx, jy + jh - 40, jw, 40);
        for (int bx = jx + 20; bx < jx + jw - 20; bx += 30) {
            int bh = 20 + r.nextInt(30);
            g2.fillRect(bx, jy + jh - 40 - bh, 22, bh);
        }
        g2.setColor(new Color(0x2A3444));
        g2.setStroke(new BasicStroke(6));
        g2.drawRect(jx, jy, jw, jh);
        g2.drawLine(jx + jw / 2, jy, jx + jw / 2, jy + jh);
        g2.drawLine(jx, jy + jh / 2, jx + jw, jy + jh / 2);

        drawSofa(g2, w - 520, h - 340);
        drawTable(g2, w - 420, h - 200);

        g2.setColor(new Color(0x2A1F14));
        g2.fillRect(w - 240, 120, 140, 90);
        g2.setColor(new Color(0x3A2A1C));
        g2.fillRect(w - 234, 126, 128, 78);
        g2.setColor(new Color(0x6B8CAE));
        g2.fillRect(w - 226, 134, 112, 62);

        Draw.tree(g2, w - 140, h - 240, 24);

        long el = System.currentTimeMillis() - t0;

        double playerX, playerY = h - 240;
        if (el < 2600) {
            double p = Math.min(1, el / 2600.0);
            playerX = -100 + p * (w * 0.28 + 100);
        } else if (el < 4000) {
            playerX = w * 0.28;
        } else {
            double p = Math.min(1, (el - 4000) / 1200.0);
            playerX = (w * 0.28) + p * (w * 0.72 - w * 0.28 - 160);
        }
        Draw.player(g2, playerX, playerY, 1.6);

        String dialog;
        Color dlgAccent;
        if (el < 2600) {
            dialog = "Aku baru pulang... tapi terasa ada yang aneh.";
            dlgAccent = Tema.CYAN;
        } else if (el < 4000) {
            dialog = "ASTAGA! Mobilku hilang dari halaman!";
            dlgAccent = Tema.RED;
        } else {
            dialog = "Cepat ambil remote pintar!";
            dlgAccent = Tema.AMBER;
        }

        int dw = 700, dh = 76;
        int dx = w / 2 - dw / 2, dy = 44;
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(dx + 4, dy + 6, dw, dh, 22, 22);
        g2.setColor(new Color(8, 12, 20, 230));
        g2.fillRoundRect(dx, dy, dw, dh, 22, 22);
        g2.setColor(dlgAccent);
        g2.setStroke(new BasicStroke(1.6f));
        g2.drawRoundRect(dx, dy, dw, dh, 22, 22);
        Fx.glowRect(g2, dx, dy, dw, dh, 22, dlgAccent, 3);
        Draw.text(g2, dialog, w / 2, dy + 48, Tema.F_H, Tema.TEXT);

        Fx.scanlines(g2, w, h, 5);
        Fx.vignette(g2, w, h, 0.6f);
    }

    private void drawSofa(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRoundRect(x + 6, y + 10, 340, 120, 22, 22);
        g2.setPaint(new GradientPaint(0, y, new Color(0x4A3760), 0, y + 100, new Color(0x2A1F3A)));
        g2.fillRoundRect(x, y, 340, 100, 22, 22);
        g2.setPaint(new GradientPaint(0, y - 50, new Color(0x3A2A50), 0, y, new Color(0x2A1F3A)));
        g2.fillRoundRect(x, y - 50, 340, 60, 22, 22);
        g2.setColor(new Color(0x5A4470));
        g2.fillRoundRect(x + 20, y + 15, 140, 70, 14, 14);
        g2.fillRoundRect(x + 180, y + 15, 140, 70, 14, 14);
        g2.setColor(new Color(0x1A1224));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y - 50, 340, 150, 22, 22);
    }

    private void drawTable(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(x + 4, y + 30, 220, 30, 10, 10);
        g2.setColor(new Color(0x2A1810));
        g2.fillRect(x + 15, y + 10, 8, 50);
        g2.fillRect(x + 195, y + 10, 8, 50);
        g2.setPaint(new GradientPaint(0, y - 10, new Color(0x7A5535),
                0, y + 15, new Color(0x4A2818)));
        g2.fillRoundRect(x, y - 10, 220, 26, 8, 8);
        g2.setColor(new Color(0x2A1810));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(x, y - 10, 220, 26, 8, 8);

        int rx = x + 80, ry = y - 28;
        Fx.glow(g2, rx + 30, ry + 10, 60, Tema.CYAN, 4);
        g2.setPaint(new GradientPaint(0, ry, new Color(0x2A3444), 0, ry + 24, new Color(0x0F1620)));
        g2.fillRoundRect(rx, ry, 60, 24, 8, 8);
        g2.setColor(new Color(0x0A1018));
        g2.fillRoundRect(rx + 4, ry + 4, 32, 14, 4, 4);
        g2.setColor(Tema.CYAN);
        g2.fillRect(rx + 7, ry + 8, 12, 3);
        g2.fillRect(rx + 7, ry + 13, 20, 2);
        g2.setColor(Tema.RED);
        g2.fillOval(rx + 42, ry + 8, 8, 8);
        g2.setColor(Tema.GREEN);
        g2.fillOval(rx + 42, ry + 18, 8, 4);
    }
}

// ============================================================
// SCENE 4: REMOTE
// ============================================================
class RemotePanel extends JPanel implements SceneLifecycle {
    private float phase = 0;
    private final javax.swing.Timer anim;

    public RemotePanel(Simulator app, Model model) {
        setBackground(Tema.BG);
        setLayout(new GridBagLayout());

        RoundedPanel box = new RoundedPanel(null, Tema.PANEL, 30, true);
        box.setPreferredSize(new Dimension(560, 620));
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(new EmptyBorder(40, 46, 40, 46));

        JPanel remote = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                Draw.antialias(g2);
                int w = getWidth(), h = getHeight();

                Fx.glowRect(g2, 10, 10, w - 20, h - 20, 30, Tema.CYAN, 6);

                g2.setPaint(new GradientPaint(0, 0, new Color(0x3A4458),
                        0, h / 2f, new Color(0x1A2130)));
                g2.fillRoundRect(0, 0, w, h, 30, 30);
                g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 40),
                        0, h / 3f, new Color(255, 255, 255, 0)));
                g2.fillRoundRect(4, 4, w - 8, h / 3, 26, 26);
                g2.setColor(new Color(0x5A6478));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 30, 30);

                g2.setColor(new Color(0x2A3344));
                g2.fillRoundRect(w / 2 - 4, -22, 8, 28, 4, 4);
                g2.setColor(Tema.CYAN);
                g2.fillOval(w / 2 - 5, -28, 10, 10);
                Fx.glow(g2, w / 2, -23, 26, Tema.CYAN, 5);

                int sx = 30, sy = 26, sw = w - 60, sh = 100;
                g2.setColor(new Color(0x0A0D12));
                g2.fillRoundRect(sx - 4, sy - 4, sw + 8, sh + 8, 14, 14);
                g2.setPaint(new GradientPaint(0, sy, new Color(0x0A1520),
                        0, sy + sh, new Color(0x040810)));
                g2.fillRoundRect(sx, sy, sw, sh, 12, 12);
                g2.setColor(Tema.CYAN);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(sx, sy, sw, sh, 12, 12);
                Fx.glowRect(g2, sx, sy, sw, sh, 12, Tema.CYAN, 3);

                int bx = sx + 20, by = sy + 24;
                g2.setPaint(new GradientPaint(0, by, Tema.RED_L, 0, by + 30, Tema.RED.darker()));
                g2.fillRoundRect(bx, by + 10, 70, 24, 6, 6);
                g2.fillRoundRect(bx + 46, by + 2, 22, 36, 6, 6);
                g2.setColor(new Color(0x0A0D12));
                g2.fillOval(bx + 10, by + 30, 12, 12);
                g2.fillOval(bx + 48, by + 30, 12, 12);

                g2.setColor(Tema.RED);
                Fx.glow(g2, bx + 140, by + 22, 40, Tema.RED, 5);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 56));
                g2.drawString("!", bx + 130, by + 45);

                g2.setFont(Tema.F_MONOB);
                g2.setColor(Tema.AMBER);
                g2.drawString("⚠ SIGNAL LOST", sx + sw - 130, sy + 30);
                g2.setColor(Tema.MUTED);
                g2.setFont(Tema.F_MONO);
                g2.drawString("GPS: TRACKING...", sx + sw - 130, sy + 50);
                g2.setColor(Tema.RED);
                g2.drawString("STATUS: STOLEN", sx + sw - 130, sy + 70);

                g2.setColor(new Color(0, 0, 0, 60));
                for (int y = sy; y < sy + sh; y += 3)
                    g2.fillRect(sx, y, sw, 1);

                int gy = sy + sh + 30;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        int bxx = 30 + j * ((w - 60) / 4) + 4;
                        int byy = gy + i * 50;
                        int bw = (w - 60) / 4 - 8;
                        int bh = 42;
                        Fx.glowRect(g2, bxx, byy, bw, bh, 8, new Color(0x22, 0xD3, 0xEE, 40), 2);
                        g2.setPaint(new GradientPaint(0, byy, new Color(0x2A3444),
                                0, byy + bh, new Color(0x141B26)));
                        g2.fillRoundRect(bxx, byy, bw, bh, 8, 8);
                        g2.setColor(new Color(255, 255, 255, 35));
                        g2.fillRoundRect(bxx + 2, byy + 2, bw - 4, bh / 3, 6, 6);
                        g2.setColor(new Color(0x3A4458));
                        g2.setStroke(new BasicStroke(1));
                        g2.drawRoundRect(bxx, byy, bw, bh, 8, 8);
                        g2.setColor(new Color(0xB0BACE));
                        g2.setFont(Tema.F_MONOB);
                        FontMetrics fm = g2.getFontMetrics();
                        String[] keys = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#" };
                        String k = keys[i * 4 + j];
                        g2.drawString(k, bxx + bw / 2 - fm.stringWidth(k) / 2, byy + bh / 2 + 4);
                    }
                }

                int lx = w / 2, ly = h - 18;
                boolean on = (System.currentTimeMillis() / 400) % 2 == 0;
                g2.setColor(on ? Tema.RED : new Color(0x3A1818));
                g2.fillOval(lx - 5, ly - 5, 10, 10);
                if (on)
                    Fx.glow(g2, lx, ly, 16, Tema.RED, 3);
            }
        };
        remote.setOpaque(false);
        remote.setPreferredSize(new Dimension(400, 380));
        remote.setMaximumSize(new Dimension(400, 380));

        JLabel warn = new JLabel("  PERINGATAN! Mobil sedang dicuri.");
        warn.setFont(new Font("Segoe UI", Font.BOLD, 17));
        warn.setForeground(Tema.RED_L);
        warn.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Tekan tombol untuk menghubungkan ke sistem kendaraan.");
        sub.setFont(Tema.F_SUB);
        sub.setForeground(Tema.MUTED);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        ModernButton takeover = new ModernButton("   AMBIL ALIH", Tema.GREEN.darker());
        takeover.setFont(new Font("Segoe UI", Font.BOLD, 15));
        takeover.setPreferredSize(new Dimension(260, 54));
        takeover.setMaximumSize(new Dimension(260, 54));
        takeover.setAlignmentX(CENTER_ALIGNMENT);
        takeover.setPulse(true);
        takeover.addActionListener(e -> app.showScene("LAPTOP"));

        box.add(remote);
        box.add(Box.createVerticalStrut(28));
        box.add(warn);
        box.add(Box.createVerticalStrut(8));
        box.add(sub);
        box.add(Box.createVerticalStrut(20));
        box.add(takeover);

        add(box);

        anim = new javax.swing.Timer(30, e -> {
            phase += 0.05f;
            repaint();
        });
        anim.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    public void removeNotify() {
        if (anim != null)
            anim.stop();
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Draw.antialias(g2);
        int w = getWidth(), h = getHeight();
        g2.setPaint(new RadialGradientPaint(new Point2D.Float(w / 2f, h / 2f),
                Math.max(w, h) * 0.75f, new float[] { 0f, 1f },
                new Color[] { new Color(0x18283A), Tema.BG }));
        g2.fillRect(0, 0, w, h);
        Random r = new Random(11);
        long t = System.currentTimeMillis();
        for (int i = 0; i < 30; i++) {
            double px = (r.nextInt(w) + t * 0.05 * (0.5 + r.nextDouble())) % w;
            double py = (r.nextInt(h) + t * 0.04 * (0.5 + r.nextDouble())) % h;
            g2.setColor(new Color(0x22, 0xD3, 0xEE, 25 + r.nextInt(50)));
            g2.fillOval((int) px, (int) py, 3, 3);
        }
        Fx.vignette(g2, w, h, 0.55f);
    }

    @Override
    public void onEnter() {
    }
}

// ============================================================
// SCENE 5: LAPTOP
// ============================================================
class LaptopPanel extends JPanel implements SceneLifecycle {
    private final Simulator app;
    private final Model model;

    private ModernButton btnTrack, btnHack, btnDoors, btnEject, btnDrive;
    private ModernButton btnUp, btnDown, btnLeft, btnRight;
    private JPanel arrowPanel;
    private Pill statusPill;
    private JLabel info1, info2;
    private JProgressBar hackBar;
    private javax.swing.Timer hackTimer;
    private JPanel mapArea;

    public LaptopPanel(Simulator app, Model model) {
        this.app = app;
        this.model = model;
        setBackground(Tema.BG);
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildMap(), BorderLayout.CENTER);
        add(buildSidebar(), BorderLayout.EAST);
        add(buildArrowBar(), BorderLayout.SOUTH);

        installArrowKeys();
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel title = new JLabel("   VEHICLE TRACKING SYSTEM");
        title.setFont(Tema.F_H);
        title.setForeground(Tema.TEXT);
        JLabel sub = new JLabel("Kota Selatan  •  Sektor 7B  •  Live Telemetry");
        sub.setFont(Tema.F_SUB);
        sub.setForeground(Tema.MUTED);
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(title);
        left.add(Box.createVerticalStrut(4));
        left.add(sub);
        statusPill = new Pill("●  MENCARI SINYAL...", Tema.AMBER);
        p.add(left, BorderLayout.WEST);
        p.add(statusPill, BorderLayout.EAST);
        return p;
    }

    private JPanel buildMap() {
        mapArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintMap((Graphics2D) g, getWidth(), getHeight());
            }
        };
        mapArea.setOpaque(false);
        mapArea.setBorder(BorderFactory.createLineBorder(Tema.BORDER, 1));
        return mapArea;
    }

    private void paintMap(Graphics2D g2, int w, int h) {
        Draw.antialias(g2);

        g2.setColor(new Color(0x0A0F18));
        g2.fillRect(0, 0, w, h);

        double sx = (double) w / Model.WORLD_W;
        double sy = (double) h / Model.WORLD_H;

        Graphics2D gs = (Graphics2D) g2.create();
        gs.scale(sx, sy);
        Draw.cityMap(gs, Model.WORLD_W, Model.WORLD_H);

        gs.setColor(new Color(0x30, 0xD1, 0x58, 30));
        gs.fillOval((int) model.houseX - 110, (int) model.houseY - 110, 220, 220);
        gs.setColor(new Color(0x30, 0xD1, 0x58, 140));
        gs.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1f, new float[] { 10, 10 }, 0));
        gs.drawOval((int) model.houseX - 110, (int) model.houseY - 110, 220, 220);
        gs.setColor(new Color(0x30, 0xD1, 0x58, 80));
        gs.setStroke(new BasicStroke(1.5f));
        gs.drawOval((int) model.houseX - 60, (int) model.houseY - 60, 120, 120);

        Draw.house(gs, model.houseX, model.houseY, 32);

        List<Point2D.Double> trail = model.trail;
        for (int i = 1; i < trail.size(); i++) {
            float a = (float) i / trail.size();
            gs.setColor(new Color(0x22, 0xD3, 0xEE, (int) (a * 140)));
            gs.setStroke(new BasicStroke(1.2f + a * 2.2f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));
            Point2D.Double p1 = trail.get(i - 1), p2 = trail.get(i);
            gs.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        }

        Color carColor = model.thiefInside ? Tema.RED : Tema.GREEN;
        long now = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
            double ph = ((now / 900.0) + i / 3.0) % 1.0;
            int r = (int) (ph * 90);
            int alpha = (int) ((1 - ph) * 100);
            gs.setColor(new Color(carColor.getRed(), carColor.getGreen(),
                    carColor.getBlue(), alpha));
            gs.setStroke(new BasicStroke(2f));
            gs.drawOval((int) model.carX - r, (int) model.carY - r, r * 2, r * 2);
        }

        Draw.car(gs, model.carX, model.carY, model.carAngle,
                carColor, model.doorsOpen, model.thiefInside, true);

        if (model.thiefVisible) {
            Draw.thiefTop(gs, model.thiefX, model.thiefY);
        }

        gs.setFont(Tema.F_MONOB);
        gs.setColor(carColor);
        String lbl = model.thiefInside ? "◉ TARGET" : "◉ MOBIL ANDA";
        FontMetrics fm = gs.getFontMetrics();
        int lx = (int) model.carX + 26;
        int ly = (int) model.carY - 32;
        gs.setColor(new Color(0, 0, 0, 160));
        gs.fillRoundRect(lx - 6, ly - 12, fm.stringWidth(lbl) + 12, 18, 6, 6);
        gs.setColor(carColor);
        gs.drawString(lbl, lx, ly);

        gs.dispose();

        paintCarInterior(g2, w - 220, h - 140, 200, 120);

        int lw = 210, lh = 100;
        g2.setColor(new Color(10, 14, 22, 210));
        g2.fillRoundRect(14, 14, lw, lh, 14, 14);
        g2.setColor(Tema.BORDER);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(14, 14, lw, lh, 14, 14);
        g2.setColor(new Color(0x22, 0xD3, 0xEE, 40));
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(14 + 12, 14, 14 + lw - 12, 14);

        g2.setFont(Tema.F_BOLD);
        g2.setColor(Tema.CYAN);
        g2.drawString("LEGENDA", 28, 34);

        g2.setColor(Tema.GREEN);
        g2.fillOval(28, 48, 12, 12);
        Fx.glow(g2, 34, 54, 14, Tema.GREEN, 2);
        g2.setColor(Tema.TEXT);
        g2.setFont(Tema.F_LABEL);
        g2.drawString("Rumah (titik aman)", 48, 58);

        g2.setColor(Tema.RED);
        g2.fillOval(28, 68, 12, 12);
        Fx.glow(g2, 34, 74, 14, Tema.RED, 2);
        g2.setColor(Tema.TEXT);
        g2.drawString("Mobil dicuri", 48, 78);

        g2.setColor(Tema.CYAN);
        g2.fillOval(28, 88, 12, 12);
        Fx.glow(g2, 34, 94, 14, Tema.CYAN, 2);
        g2.setColor(Tema.TEXT);
        g2.drawString("Jejak pergerakan", 48, 98);

        Fx.scanlines(g2, w, h, 8);
        Fx.vignette(g2, w, h, 0.35f);
    }

    private void paintCarInterior(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x + 4, y + 6, w, h, 14, 14);
        g2.setColor(new Color(10, 14, 22, 235));
        g2.fillRoundRect(x, y, w, h, 14, 14);
        g2.setColor(Tema.BORDER_L);
        g2.setStroke(new BasicStroke(1.3f));
        g2.drawRoundRect(x, y, w, h, 14, 14);

        g2.setFont(Tema.F_BOLD);
        g2.setColor(Tema.CYAN);
        g2.drawString("◉ KABIN", x + 12, y + 20);

        g2.setColor(new Color(0x0A1018));
        g2.fillRoundRect(x + 12, y + 28, w - 24, h - 60, 10, 10);
        g2.setColor(new Color(0x22, 0xD3, 0xEE, 30));
        g2.drawRoundRect(x + 12, y + 28, w - 24, h - 60, 10, 10);

        g2.setColor(new Color(0x1A2130));
        g2.fillRoundRect(x + 8, y + h - 30, w - 16, 22, 8, 8);

        g2.setColor(new Color(0x2A3140));
        g2.setStroke(new BasicStroke(3.5f));
        g2.drawOval(x + 18, y + h - 50, 30, 30);
        g2.setColor(new Color(0x141B26));
        g2.fillOval(x + 30, y + h - 38, 6, 6);

        if (model.thiefInside) {
            int cx = x + w / 2 + 20, cy = y + h / 2 - 8;
            Fx.glow(g2, cx, cy, 40, Tema.RED, 4);
            g2.setColor(new Color(0x2A2F3A));
            g2.fillRoundRect(cx - 18, cy - 6, 36, 36, 12, 12);
            g2.setColor(new Color(0xF2C9A0));
            g2.fillOval(cx - 13, cy - 28, 26, 26);
            g2.setColor(new Color(0x0F131A));
            g2.fillArc(cx - 15, cy - 32, 30, 20, 0, 180);
            g2.fillRect(cx - 17, cy - 18, 34, 3);
            g2.setColor(Tema.RED);
            g2.fillRect(cx - 14, cy - 15, 28, 4);
            g2.setColor(new Color(0xF2C9A0));
            g2.fillOval(x + 24, y + h - 44, 9, 9);
            g2.fillOval(x + 44, y + h - 44, 9, 9);

            g2.setColor(Tema.RED_L);
            g2.setFont(Tema.F_BOLD);
            g2.drawString("⚠ PENCURI DI DALAM", x + 12, y + h - 6);
        } else if (model.thiefVisible) {
            Draw.thiefSide(g2, x + w / 2, y + h - 32, 0.75,
                    System.currentTimeMillis() / 100.0);
        } else {
            g2.setColor(Tema.GREEN);
            g2.setFont(Tema.F_BOLD);
            String s = "✔  KABIN AMAN";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(s, x + (w - fm.stringWidth(s)) / 2, y + h / 2);
            Fx.glow(g2, x + w / 2, y + h / 2 - 6, 50, Tema.GREEN, 3);
        }
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setOpaque(false);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setPreferredSize(new Dimension(340, 0));

        RoundedPanel tele = new RoundedPanel(null, Tema.PANEL, 18, true);
        tele.setLayout(new BoxLayout(tele, BoxLayout.Y_AXIS));
        tele.setBorder(new EmptyBorder(16, 18, 16, 18));
        tele.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel t = new JLabel("  STATUS KENDARAAN");
        t.setFont(Tema.F_BOLD);
        t.setForeground(Tema.CYAN);
        t.setAlignmentX(LEFT_ALIGNMENT);

        info1 = new JLabel("POSISI  : -");
        info1.setFont(Tema.F_MONO);
        info1.setForeground(Tema.TEXT_DIM);
        info1.setAlignmentX(LEFT_ALIGNMENT);
        info2 = new JLabel("MODE    : -");
        info2.setFont(Tema.F_MONO);
        info2.setForeground(Tema.TEXT_DIM);
        info2.setAlignmentX(LEFT_ALIGNMENT);

        tele.add(t);
        tele.add(Box.createVerticalStrut(10));
        tele.add(info1);
        tele.add(Box.createVerticalStrut(4));
        tele.add(info2);

        RoundedPanel act = new RoundedPanel(null, Tema.PANEL, 18, true);
        act.setLayout(new BoxLayout(act, BoxLayout.Y_AXIS));
        act.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t2 = new JLabel("  AKSES BERTAHAP");
        t2.setFont(Tema.F_BOLD);
        t2.setForeground(Tema.CYAN);
        t2.setAlignmentX(LEFT_ALIGNMENT);

        btnTrack = makeStep("1.    LACAK LOKASI", new Color(0x1F6FEB));
        btnHack = makeStep("2.    AMBIL ALIH KENDARAAN", Tema.PURPLE.darker());
        btnDoors = makeStep("3.    BUKA PINTU OTOMATIS", Tema.CYAN.darker());
        btnEject = makeStep("4.    KELUARKAN PENCURI", Tema.AMBER.darker());
        btnDrive = makeStep("5.    KONTROL MOBIL", Tema.GREEN.darker());

        hackBar = new JProgressBar(0, 100);
        hackBar.setForeground(Tema.CYAN);
        hackBar.setBackground(Tema.TRACK);
        hackBar.setBorderPainted(false);
        hackBar.setStringPainted(false);
        hackBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
        hackBar.setVisible(false);
        hackBar.setAlignmentX(LEFT_ALIGNMENT);
        hackBar.setBorder(BorderFactory.createEmptyBorder());

        act.add(t2);
        act.add(Box.createVerticalStrut(12));
        act.add(btnTrack);
        act.add(Box.createVerticalStrut(7));
        act.add(btnHack);
        act.add(Box.createVerticalStrut(7));
        act.add(hackBar);
        act.add(Box.createVerticalStrut(7));
        act.add(btnDoors);
        act.add(Box.createVerticalStrut(7));
        act.add(btnEject);
        act.add(Box.createVerticalStrut(7));
        act.add(btnDrive);

        side.add(tele);
        side.add(Box.createVerticalStrut(14));
        side.add(act);
        side.add(Box.createVerticalGlue());

        btnTrack.addActionListener(e -> {
            if (!unlock(0))
                return;
            status("●  TRACKING AKTIF — target terdeteksi", Tema.CYAN);
            btnTrack.setEnabled(false);
            btnTrack.setText("✔   LOKASI TERLACAK");
            model.stage = 1;
            refreshButtons();
        });

        btnHack.addActionListener(e -> {
            if (!unlock(1))
                return;
            if (model.hacking)
                return;
            model.hacking = true;
            hackBar.setVisible(true);
            hackBar.setValue(0);
            btnHack.setEnabled(false);
            status("●  MENEMBUS FIREWALL...", Tema.PURPLE);
            if (hackTimer != null)
                hackTimer.stop();
            hackTimer = new javax.swing.Timer(40, ev -> {
                model.hackProgress += 0.012;
                if (model.hackProgress >= 1) {
                    model.hackProgress = 1;
                    hackBar.setValue(100);
                    ((javax.swing.Timer) ev.getSource()).stop();
                    javax.swing.Timer fin = new javax.swing.Timer(400, x -> completeHack());
                    fin.setRepeats(false);
                    fin.start();
                } else {
                    hackBar.setValue((int) (model.hackProgress * 100));
                }
            });
            hackTimer.start();
        });

        btnDoors.addActionListener(e -> {
            if (!unlock(2))
                return;
            model.doorsOpen = true;
            btnDoors.setEnabled(false);
            btnDoors.setText("✔   PINTU TERBUKA");
            status("●  PINTU DIBUKA OTOMATIS", Tema.CYAN);
            model.stage = 3;
            refreshButtons();
        });

        btnEject.addActionListener(e -> {
            if (!unlock(3))
                return;
            model.ejectThief();
            btnEject.setEnabled(false);
            btnEject.setText("✔   PENCURI KELUAR");
            status("●  PENCURI DIKELUARKAN DARI KABIN", Tema.AMBER);
            model.stage = 4;
            refreshButtons();
        });

        btnDrive.addActionListener(e -> {
            if (!unlock(4))
                return;
            btnDrive.setEnabled(false);
            btnDrive.setText("✔   KONTROL PENUH AKTIF");
            status("●  KENDALI PENUH — arahkan mobil pulang", Tema.GREEN);
            arrowPanel.setVisible(true);
            model.stage = 5;
            refreshButtons();
        });

        return side;
    }

    private ModernButton makeStep(String text, Color c) {
        ModernButton b = new ModernButton(text, c);
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setEnabled(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return b;
    }

    private boolean unlock(int needStage) {
        return model.stage == needStage && !model.thiefEscaping;
    }

    private void completeHack() {
        model.hacking = false;
        btnHack.setText("✔   KENDALI DIAMBIL ALIH");
        status("●  KENDALI BERHASIL DIAMBIL ALIH", Tema.GREEN);
        model.stage = 2;
        hackBar.setVisible(false);
        refreshButtons();
    }

    private void refreshButtons() {
        if (model.stage == 1)
            btnHack.setEnabled(true);
        if (model.stage == 2)
            btnDoors.setEnabled(true);
        if (model.stage == 3)
            btnEject.setEnabled(true);
        if (model.stage == 4)
            btnDrive.setEnabled(true);
    }

    private JPanel buildArrowBar() {
        arrowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        arrowPanel.setOpaque(false);
        arrowPanel.setVisible(false);

        btnUp = new ModernButton("   MAJU", Tema.GREEN.darker());
        btnDown = new ModernButton("   MUNDUR", Tema.AMBER.darker());
        btnLeft = new ModernButton("  KIRI", new Color(0x1F6FEB));
        btnRight = new ModernButton("  KANAN", new Color(0x1F6FEB));

        btnUp.setPreferredSize(new Dimension(150, 48));
        btnDown.setPreferredSize(new Dimension(150, 48));
        btnLeft.setPreferredSize(new Dimension(150, 48));
        btnRight.setPreferredSize(new Dimension(150, 48));

        arrowPanel.add(btnLeft);
        arrowPanel.add(btnUp);
        arrowPanel.add(btnDown);
        arrowPanel.add(btnRight);
        return arrowPanel;
    }

    private void installArrowKeys() {
        pressHold(btnUp, () -> model.throttle = 1, () -> model.throttle = 0);
        pressHold(btnDown, () -> model.throttle = -1, () -> model.throttle = 0);
        pressHold(btnLeft, () -> model.steer = -1, () -> model.steer = 0);
        pressHold(btnRight, () -> model.steer = 1, () -> model.steer = 0);

        InputMap im = app.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = app.getRootPane().getActionMap();
        bindKey(im, am, "W", KeyEvent.VK_W, () -> model.throttle = 1, () -> model.throttle = 0);
        bindKey(im, am, "S", KeyEvent.VK_S, () -> model.throttle = -1, () -> model.throttle = 0);
        bindKey(im, am, "A", KeyEvent.VK_A, () -> model.steer = -1, () -> model.steer = 0);
        bindKey(im, am, "D", KeyEvent.VK_D, () -> model.steer = 1, () -> model.steer = 0);
        bindKey(im, am, "UP", KeyEvent.VK_UP, () -> model.throttle = 1, () -> model.throttle = 0);
        bindKey(im, am, "DOWN", KeyEvent.VK_DOWN, () -> model.throttle = -1, () -> model.throttle = 0);
        bindKey(im, am, "LEFT", KeyEvent.VK_LEFT, () -> model.steer = -1, () -> model.steer = 0);
        bindKey(im, am, "RIGHT", KeyEvent.VK_RIGHT, () -> model.steer = 1, () -> model.steer = 0);
    }

    private void bindKey(InputMap im, ActionMap am, String name, int code,
            Runnable on, Runnable off) {
        im.put(KeyStroke.getKeyStroke(code, 0, false), name + "_p");
        im.put(KeyStroke.getKeyStroke(code, 0, true), name + "_r");
        am.put(name + "_p", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                on.run();
            }
        });
        am.put(name + "_r", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                off.run();
            }
        });
    }

    private void pressHold(JButton b, Runnable on, Runnable off) {
        b.getModel().addChangeListener(e -> {
            if (b.getModel().isPressed())
                on.run();
            else
                off.run();
        });
    }

    @Override
    public void onEnter() {
        model.carX = 200;
        model.carY = 160;
        model.carAngle = 0;
        model.carSpeed = 0;
        model.trail.clear();
        model.stage = 0;
        model.thiefInside = true;
        model.thiefVisible = false;
        model.thiefEscaping = false;
        model.doorsOpen = false;
        model.hacking = false;
        model.hackProgress = 0;
        model.thiefWp = 0;

        btnTrack.setEnabled(true);
        btnTrack.setText("1.    LACAK LOKASI");
        btnHack.setEnabled(false);
        btnHack.setText("2.     AMBIL ALIH KENDARAAN");
        btnDoors.setEnabled(false);
        btnDoors.setText("3.    BUKA PINTU OTOMATIS");
        btnEject.setEnabled(false);
        btnEject.setText("4.    KELUARKAN PENCURI");
        btnDrive.setEnabled(false);
        btnDrive.setText("5.    KONTROL MOBIL");

        hackBar.setVisible(false);
        hackBar.setValue(0);
        arrowPanel.setVisible(false);
        status("●  TRACKING — menunggu perintah", Tema.AMBER);

        javax.swing.Timer t = new javax.swing.Timer(80, e -> {
            if (model.scene != Scene.LAPTOP) {
                ((javax.swing.Timer) e.getSource()).stop();
                return;
            }
            updateInfo();
            if (checkWin()) {
                ((javax.swing.Timer) e.getSource()).stop();
                app.showScene("WIN");
            }
        });
        t.start();
    }

    private boolean checkWin() {
        if (model.stage < 5)
            return false;
        double dx = model.carX - model.houseX;
        double dy = model.carY - model.houseY;
        return Math.hypot(dx, dy) < 80 && Math.abs(model.carSpeed) < 1.2;
    }

    private void updateInfo() {
        info1.setText(String.format("POSISI  : X=%d  Y=%d", (int) model.carX, (int) model.carY));
        String mode;
        if (model.stage == 0)
            mode = "TRACKING";
        else if (model.stage == 1)
            mode = "HACKING...";
        else if (model.stage == 2)
            mode = "CONTROLLED";
        else if (model.stage == 3)
            mode = "DOORS OPEN";
        else if (model.stage == 4)
            mode = "EJECTING";
        else
            mode = "FREE DRIVE";
        info2.setText("MODE    : " + mode);
        if (mapArea != null)
            mapArea.repaint();
    }

    private void status(String msg, Color c) {
        statusPill.setText(msg);
        statusPill.setAccent(c);
    }
}

// ============================================================
// SCENE 6: WIN
// ============================================================
class WinPanel extends JPanel implements SceneLifecycle {
    private float phase = 0;
    private final javax.swing.Timer anim;

    public WinPanel(Simulator app, Model model) {
        setBackground(Tema.BG);
        setLayout(new GridBagLayout());

        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));

        JLabel star = new JLabel("🏆");
        star.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 110));
        star.setAlignmentX(CENTER_ALIGNMENT);

        JLabel t = new JLabel("MOBIL BERHASIL DIAMANKAN");
        t.setFont(new Font("Segoe UI", Font.BOLD, 38));
        t.setForeground(Tema.GREEN);
        t.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Pencuri telah dikeluarkan dan kendaraan kembali ke rumah dengan selamat.");
        sub.setFont(Tema.F_SUB);
        sub.setForeground(Tema.MUTED);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        JPanel stats = new JPanel();
        stats.setOpaque(false);
        stats.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        stats.add(statBadge("🛡", "KEAMANAN", "TERJAGA", Tema.GREEN));
        stats.add(statBadge("⏱", "RESPONS", "< 10 dtk", Tema.CYAN));
        stats.add(statBadge("⭐", "RATING", "SEMPURNA", Tema.GOLD));

        JPanel btnRow = new JPanel();
        btnRow.setOpaque(false);
        btnRow.setLayout(new FlowLayout(FlowLayout.CENTER, 16, 0));

        ModernButton lagi = new ModernButton("   MAIN LAGI", Tema.CYAN.darker());
        lagi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lagi.setPreferredSize(new Dimension(190, 54));
        lagi.addActionListener(e -> {
            model.reset();
            app.showScene("HOME");
        });

        ModernButton selesai = new ModernButton("   SELESAI", new Color(0x4B5563));
        selesai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selesai.setPreferredSize(new Dimension(190, 54));
        selesai.addActionListener(e -> System.exit(0));

        btnRow.add(lagi);
        btnRow.add(selesai);

        wrap.add(star);
        wrap.add(Box.createVerticalStrut(14));
        wrap.add(t);
        wrap.add(Box.createVerticalStrut(10));
        wrap.add(sub);
        wrap.add(Box.createVerticalStrut(34));
        wrap.add(stats);
        wrap.add(Box.createVerticalStrut(40));
        wrap.add(btnRow);

        add(wrap);

        anim = new javax.swing.Timer(30, e -> {
            phase += 0.03f;
            repaint();
        });
        anim.start();
    }

    private JPanel statBadge(String icon, String label, String value, Color c) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                Draw.antialias(g2);
                Fx.glowRect(g2, 4, 4, getWidth() - 8, getHeight() - 8, 12, c, 3);
                g2.setColor(new Color(15, 20, 30, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(c);
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(150, 90));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(12, 12, 12, 12));
        JLabel i = new JLabel(icon);
        i.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        i.setAlignmentX(CENTER_ALIGNMENT);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        l.setForeground(Tema.MUTED);
        l.setAlignmentX(CENTER_ALIGNMENT);
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 14));
        v.setForeground(c);
        v.setAlignmentX(CENTER_ALIGNMENT);
        p.add(i);
        p.add(Box.createVerticalStrut(4));
        p.add(l);
        p.add(Box.createVerticalStrut(2));
        p.add(v);
        return p;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    public void removeNotify() {
        if (anim != null)
            anim.stop();
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Draw.antialias(g2);
        int w = getWidth(), h = getHeight();

        g2.setPaint(new RadialGradientPaint(new Point2D.Float(w / 2f, h / 2f),
                Math.max(w, h) * 0.75f, new float[] { 0f, 1f },
                new Color[] { new Color(0x0E2A1A), Tema.BG }));
        g2.fillRect(0, 0, w, h);

        Random r = new Random(2024);
        long t = System.currentTimeMillis();
        Color[] cols = { Tema.GREEN, Tema.CYAN, Tema.AMBER, Tema.PURPLE, Tema.GOLD };
        for (int i = 0; i < 60; i++) {
            double cx = (r.nextInt(w) + t * 0.03 * (0.5 + r.nextDouble())) % w;
            double cy = (r.nextInt(h) + t * 0.06 * (0.5 + r.nextDouble())) % h;
            g2.setColor(cols[i % cols.length]);
            g2.fillRect((int) cx, (int) cy, 4, 8);
        }

        Fx.glow(g2, w / 2, h / 2, 300, Tema.GREEN, 6);
        Fx.vignette(g2, w, h, 0.6f);
    }

    @Override
    public void onEnter() {
    }
}

// ============================================================
// MAIN APP
// ============================================================
public class Simulator extends JFrame {
    private final Model model = new Model();
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private JPanel currentPanel;
    private final javax.swing.Timer loop;

    public Simulator() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        setTitle("Smart Car Security — Simulator");
        setSize(1360, 860);
        setMinimumSize(new Dimension(1200, 760));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        root.setBackground(Tema.BG);

        root.add(new HomePanel(this, model), "HOME");
        root.add(new TheftPanel(this, model), "THEFT");
        root.add(new RealizePanel(this, model), "REALIZE");
        root.add(new RemotePanel(this, model), "REMOTE");
        root.add(new LaptopPanel(this, model), "LAPTOP");
        root.add(new WinPanel(this, model), "WIN");

        setContentPane(root);

        loop = new javax.swing.Timer(16, e -> {
            model.update(0.016);
            if (currentPanel != null)
                currentPanel.repaint();
        });
        loop.start();

        showScene("HOME");
    }

    public void showScene(String name) {
        cards.show(root, name);
        model.scene = Scene.valueOf(name);
        for (Component c : root.getComponents()) {
            if (c.isVisible()) {
                currentPanel = (JPanel) c;
                if (c instanceof SceneLifecycle)
                    ((SceneLifecycle) c).onEnter();
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Simulator().setVisible(true));
    }
}